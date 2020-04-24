package com.github.sj853.imgify;

import org.apache.tika.Tika;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Scanner;

/**
 * 图片处理
 *
 * @author elemeNT
 * @version 1.0
 * created on 2020/4/22 16:08
 */
public class ImageProcess {

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


                BufferedImage sourceImage = ImageIO.read(source);

                if (sourceImage == null) {
                    throw new Exception("文件不是图片文件");
                }

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

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        System.out.println("请输入要处理图片或目录绝对路径：");
        String sourceDirectoryPath = sc.nextLine();
        System.out.println("请输入要存放的路径：");
        String destDirectoryPath = sc.nextLine();

        byte[] bytes = ImageProcess.clearExif(FileSystems.getDefault().getPath(sourceDirectoryPath).toFile());

        FileOutputStream fileOutputStream = new FileOutputStream(FileSystems.getDefault().getPath(destDirectoryPath).toFile());

        fileOutputStream.write(bytes);

        fileOutputStream.flush();

        fileOutputStream.close();
    }

}
