package com.devspark.palabra_clara.util;

import lombok.Getter;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.VideoAttributes;
import ws.schild.jave.info.VideoSize;

@Getter
public enum ConfiguracionesCalidad {
        MP4(createMP4VideoAttributos(), createMP4AudioAttributos(), StaticConstants.TIPO_EXTENSION_MP4),
        WEBM(createWebMVideoAttributos(), createWebMAudioAttributos(), StaticConstants.TIPO_EXTENSION_WEBM),
        WEBM_HORIZONTAL(createWebMVideoHorizontalAttributes(), createWebMAudioAttributos(), StaticConstants.TIPO_EXTENSION_WEBM),
        LOW_QUALITY_MP4(createLowQualityVideoAttributos(), createLowQualityAudioAttributos(), StaticConstants.TIPO_EXTENSION_MP4),
        HD_MP4(createHDVideoAttributos(), createHDAudioAttributos(), StaticConstants.TIPO_EXTENSION_MP4);

        private final VideoAttributes videoAttributes;
        private final AudioAttributes audioAttributes;
        private final String extension;

        ConfiguracionesCalidad(VideoAttributes videoAttributes, AudioAttributes audioAttributes, String extension) {
            this.videoAttributes = videoAttributes;
            this.audioAttributes = audioAttributes;
            this.extension = extension;
        }

        private static VideoAttributes createMP4VideoAttributos() {
            VideoAttributes attrs = new VideoAttributes();
            attrs.setCodec(StaticConstants.CODEC_MP4);
            attrs.setBitRate(2_000_000);
            attrs.setFrameRate(30);
            attrs.setSize(new VideoSize(1280, 720));
            attrs.setQuality(80);
            return attrs;
        }

        private static AudioAttributes createMP4AudioAttributos() {
            AudioAttributes attrs = new AudioAttributes();
            attrs.setCodec(StaticConstants.CODEC_AUDIO_MP4);
            attrs.setBitRate(128_000);
            attrs.setChannels(2);
            attrs.setSamplingRate(44_100);
            return attrs;
        }

        private static VideoAttributes createWebMVideoAttributos() {
            VideoAttributes attrs = new VideoAttributes();
            attrs.setCodec(StaticConstants.CODEC_WEBM);
            attrs.setBitRate(500_000);
            attrs.setFrameRate(30);
            attrs.setSize(new VideoSize(1280, 720));
            return attrs;
        }

        private static VideoAttributes createWebMVideoHorizontalAttributes() {
            VideoAttributes attrs = new VideoAttributes();
            attrs.setCodec(StaticConstants.CODEC_WEBM);
            attrs.setBitRate(500_000);
            attrs.setFrameRate(30);
            attrs.setSize(new VideoSize(720, 1280));
            return attrs;
        }

        private static AudioAttributes createWebMAudioAttributos() {
            AudioAttributes attrs = new AudioAttributes();
            attrs.setCodec(StaticConstants.CODEC_AUDIO_WEBM);
            attrs.setBitRate(128_000);
            attrs.setChannels(2);
            attrs.setSamplingRate(44_100);
            return attrs;
        }

        private static VideoAttributes createLowQualityVideoAttributos() {
            VideoAttributes attrs = new VideoAttributes();
            attrs.setCodec(StaticConstants.CODEC_LOW_QUALITY_MP4);
            attrs.setBitRate(500_000);
            attrs.setFrameRate(24);
            attrs.setSize(new VideoSize(854, 480));
            attrs.setQuality(70);
            return attrs;
        }

        private static AudioAttributes createLowQualityAudioAttributos() {
            AudioAttributes attrs = new AudioAttributes();
            attrs.setCodec(StaticConstants.CODEC_AUDIO_LOW_QUALITY_MP4);
            attrs.setBitRate(96_000);
            attrs.setChannels(2);
            attrs.setSamplingRate(44_100);
            return attrs;
        }

        private static VideoAttributes createHDVideoAttributos() {
        VideoAttributes attrs = new VideoAttributes();
        attrs.setCodec(StaticConstants.CODEC_HD_MP4);
        attrs.setBitRate(4_000_000); // 4Mbps para HD
        attrs.setFrameRate(60);      // 60fps para HD
        attrs.setSize(new VideoSize(1920, 1080)); // Full HD
        attrs.setQuality(90);
        return attrs;
    }

        private static AudioAttributes createHDAudioAttributos() {
        AudioAttributes attrs = new AudioAttributes();
        attrs.setCodec(StaticConstants.CODEC_AUDIO_MP4);
        attrs.setBitRate(192_000); // Mejor calidad de audio para HD
        attrs.setChannels(2);
        attrs.setSamplingRate(48_000); // Mayor tasa de muestreo
        return attrs;
    }
}