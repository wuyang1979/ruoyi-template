package com.ruoyi.web.controller.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.ImageUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Hashtable;
import java.util.Map;

/**
 * @author yuanjie
 * @date 2022/9/7
 */
@Api(tags = "二维码生成")
@RestController
@RequestMapping("")
public class QRCodeController {
    private static final String CHARSET = "utf-8";
    private static final String FORMAT_NAME = "JPG";
    // 二维码尺寸
    private static final int QRCODE_SIZE = 300;

    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;
    private static final String qrUrlPrefix = "https://36.153.107.210:18001/index.html#/handleResolve/";
    private static final String qrRoute = "/home/local/wlscp/fileUpload/images/handleQr/";

    @Value("${ruoyi.url-prefix}")
    private String urlPrefix;

    /**
     * 生成二维码
     */
    @ApiOperation("生成二维码")
    @GetMapping("/qrcode")
    public void getQrcode(String url, HttpServletResponse response) {
        BufferedImage image;
        try {
            image = createImage(url);
            ImageIO.write(image, FORMAT_NAME, response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成二维码图片
     *
     * @param url 扫描二维码跳转地址
     * @return
     * @throws Exception
     */
    public static BufferedImage createImage(String url) throws Exception {
        Hashtable hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, CHARSET);
        hints.put(EncodeHintType.MARGIN, 1);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(url, BarcodeFormat.QR_CODE, QRCODE_SIZE, QRCODE_SIZE,
                hints);
        int width = bitMatrix.getWidth();
        int height = bitMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF);
            }
        }
        return image;
    }

    /**
     * 生成二维码并返回二维码地址
     */
    @ApiOperation("生成二维码并返回二维码地址")
    @PostMapping("/generateQr")
    public AjaxResult generateQr(@RequestBody Map param) throws Exception {
        // 这里是URL，扫描之后就跳转到这个界面
        //type区分生成类型：1-二维码；2-下载地址
        int type = Integer.parseInt(param.get("type").toString());
        String text = "";
        if (type == 1) {
            if (StringUtils.isEmpty(param.get("handleCode").toString())) {
                return AjaxResult.error("handleCode码不能为空");
            }
            String handleCode = param.get("handleCode").toString();
            text = qrUrlPrefix + handleCode;
        } else if (type == 2) {
            String downloadUrl = param.get("downloadUrl").toString();
            if (StringUtils.isEmpty(downloadUrl)) {
                return AjaxResult.error("下载地址不能为空");
            }
            text = downloadUrl;
        }

        int width = 150;
        int height = 150;
        // 二维码图片格式
        String format = "jpg";

        // 设置编码，防止中文乱码
        Hashtable<EncodeHintType, Object> ht = new Hashtable<EncodeHintType, Object>();
        ht.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 设置二维码参数(编码内容，编码类型，图片宽度，图片高度,格式)
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, ht);

        int bWidth = bitMatrix.getWidth();
        int bHeight = bitMatrix.getHeight();
        // 建立图像缓冲器
        BufferedImage image = new BufferedImage(bWidth, bHeight, BufferedImage.TYPE_3BYTE_BGR);
        for (int x = 0; x < bWidth; x++) {
            for (int y = 0; y < bHeight; y++) {
                image.setRGB(x, y, bitMatrix.get(x, y) ? BLACK : WHITE);
            }
        }
        String fileName = ImageUtils.randomFileName() + "." + format;
        String fileRoute = qrRoute + fileName;
        File qrFile = new File(fileRoute);
        if (!qrFile.exists()) {
            qrFile.createNewFile();
        }
        // 设置下载文件的mineType，告诉浏览器下载文件类型
        ImageIO.write(image, format, qrFile);
        String qrUrl = urlPrefix + "/images/handleQr/" + fileName;
        return AjaxResult.success(qrUrl);
    }
}