package com.github.sj853.imgify;

import org.apache.tika.Tika;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Optional;

/**
 * 图片处理
 *
 * @author elemeNT
 * @version 1.0
 * created on 2020/4/22 16:08
 */
public class ImageProcess {

    private static final Logger logger = LoggerFactory.getLogger(ImageProcess.class);

    private static final String[] IMAGE_MIME_TYPES = {"image/tiff", "image/jpeg", "image/png", "image/webp", "image/gif", "image/bmp"};

    /**
     * 清理Exif
     *
     * @param source 源图片
     * @return 处理后图片二进制
     * @throws IOException 图片不存在或IO操作失败
     */
    public static byte[] clearExif(final File source) throws IOException {
        try {

            Tika tika = new Tika();

            String mimeType = tika.detect(source);

            if (Arrays.asList(IMAGE_MIME_TYPES).contains(mimeType)) {
                BufferedImage sourceImage = Optional.ofNullable(ImageIO.read(source)).orElseThrow(() -> new Exception("文件不是图片文件"));

                ByteArrayOutputStream os = new ByteArrayOutputStream();

                ImageIO.write(sourceImage, mimeType.substring(6), os);

                os.flush();

                sourceImage.flush();

                return os.toByteArray();
            } else {
                throw new Exception("文件不是图片文件");
            }
        } catch (Exception e) {
            return Files.readAllBytes(source.toPath());
        }
    }

}
