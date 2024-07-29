package com.aibees.service.maria.multipart.utils;

import com.aibees.service.maria.multipart.domain.vo.ImageFileVo;
import org.springframework.stereotype.Component;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@Component
public class PhotoUtils {

    private static final String WIDE = "wide";
    private static final String NARR = "narrow";
    private static final int IMG_SIZE = 6000;

    public File getPortraitImage(ImageFileVo fileVo) throws IOException {
        BufferedImage background = ImageIO.read(new File("D:\\background.jpg"));
        BufferedImage picture = ImageIO.read(new File("D:\\DSC05577.JPG"));

        String picType = picture.getWidth() > picture.getHeight() ? WIDE : NARR;

        return null;
    }

    private Graphics2D getBaseImage() {
        BufferedImage mergedImage = new BufferedImage(IMG_SIZE, IMG_SIZE, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = (Graphics2D) mergedImage.getGraphics();

        graphics.setBackground(Color.WHITE);

        return graphics;
    }
}
