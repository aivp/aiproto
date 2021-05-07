package com.ling5821.aiproto.schema;

import com.ling5821.aiproto.Schema;
import io.netty.buffer.ByteBuf;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author lsj
 * @date 2021/1/22 19:03
 */
public class StringSchema {
    public static class Chars implements Schema<String> {
        private static volatile Map<Object, Chars> cache = new HashMap<>();
        public final Charset charset;
        private final byte pad;

        public Chars(byte pad, String charset) {
            this.pad = pad;
            this.charset = Charset.forName(charset);
        }

        public static Schema<String> getInstance(byte pad, String charset) {
            String key = String.valueOf((char)pad) + '/' + charset;
            Chars instance;
            if ((instance = cache.get(key)) == null) {
                synchronized (cache) {
                    if ((instance = cache.get(key)) == null) {
                        instance = new Chars(pad, charset);
                        cache.put(key, instance);
                    }
                }

            }
            return instance;
        }

        @Override
        public String readFrom(ByteBuf input) {
            return readFrom(input, input.readableBytes());
        }

        @Override
        public void writeTo(ByteBuf output, String message) {
            output.writeBytes(message.getBytes(charset));
        }

        @Override
        public String readFrom(ByteBuf input, int length) {
            int len = length > 0 ? length : input.readableBytes();
            byte[] bytes = new byte[len];
            input.readBytes(bytes);
            int st = 0;
            while ((st < len) && (bytes[st] == pad)) {
                st++;
            }
            while ((st < len) && (bytes[len - 1] == pad)) {
                len--;
            }
            return new String(bytes, st, len - st, charset);
        }

        @Override
        public void writeTo(ByteBuf output, int length, String message) {
            byte[] bytes = message.getBytes();
            if (length > 0) {
                int srcPos = length - bytes.length;
                if (srcPos > 0) {
                    byte[] pads = new byte[srcPos];
                    if (this.pad != 0x00) {
                        Arrays.fill(pads, pad);
                    }
                    output.writeBytes(bytes);
                    output.writeBytes(pads);
                } else if (srcPos < 0) {
                    output.writeBytes(bytes, -srcPos, length);
                } else {
                    output.writeBytes(bytes);
                }
            } else {
                output.writeBytes(bytes);
            }
        }
    }
}
