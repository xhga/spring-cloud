package com.example.demo.model.pojo;

import com.itextpdf.text.Image;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

@Data
public class PdfParam {

    private InputStream inputStream;   // pdf文件流
    private OutputStream outputStream; // pdf输出流

    private String title;    // 标题
    private String author;   // 作者
    private String subject;  // 主题
    private String keywords; // 关键字
    private String creator;  // 创建者

    private Map<String, String> dataParamMap; // 表单参数
    private Map<String, Image> imgParamMap;   // 表单域图片

    private boolean encryption; // 是否设置权限
    private String userPass;    // 使用者密码
    private String ownerPass;   // 所有者密码
    private int permissions;    // 权限        example: PdfWriter.ALLOW_PRINTING

    public PdfParam(InputStream inputStream, OutputStream outputStream) {
        this.inputStream = inputStream;
        this.outputStream = outputStream;
    }

    public void close() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
