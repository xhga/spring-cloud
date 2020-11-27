package com.example.demo.util;

import com.example.demo.model.pojo.PdfParam;
import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PDF工具类
 */
public class PdfUtil {

    /**
     * 获取模板内容 (directory)
     * @param templateDirectory 模板文件夹
     * @param templateName      模板文件名
     * @param paramMap          模板参数
     * @return
     * @throws Exception
     */
    public static String getHtmlTemplateContentFromFolder(String templateDirectory, String templateName, Map<String, Object> paramMap) throws Exception {
        Configuration configurationFolder = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            configurationFolder.setDirectoryForTemplateLoading(new File(templateDirectory));
        } catch (Exception e) {
            System.out.println("-- exception --");
        }
        Template template = configurationFolder.getTemplate(templateName,"UTF-8");
        return getHtmlTemplateContent(template, paramMap);
    }

    /**
     * 获取模板内容 (templates)
     * @param templateName      模板文件名
     * @param paramMap          模板参数
     * @return
     * @throws Exception
     */
    public static String getHtmlTemplateContentFromResources(String templateName, Map<String, Object> paramMap) throws Exception {
        Configuration configuration = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        try {
            ClassLoader classLoader = PdfUtil.class.getClassLoader();
            URL resource = classLoader.getResource("templates");
            configuration.setDirectoryForTemplateLoading(new File(resource.toURI()));
        } catch (Exception e) {
            System.out.println("-- exception --");
        }
        Template template = configuration.getTemplate(templateName,"UTF-8");
        return getHtmlTemplateContent(template, paramMap);
    }

    private static String getHtmlTemplateContent(Template template, Map<String, Object> paramMap) throws Exception{
        Writer out = new StringWriter();
        template.process(paramMap, out);
        out.flush();
        out.close();
        return out.toString();
    }

    /**
     * HTML 转 PDF
     * @param content     html内容
     * @param outPath     输出pdf路径
     * @return 是否创建成功
     */
    public static boolean html2Pdf(String content, String outPath) {
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(outPath));
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(content.getBytes()), Charset.forName("UTF-8"));
            document.close();
        } catch (Exception e) {
            System.out.println("生成模板内容失败"+e.fillInStackTrace());
            return false;
        }
        return true;
    }

    /**
     * HTML 转 PDF
     * @param content html内容
     * @return PDF字节数组
     */
    public static byte[] html2Pdf(String content) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            Document document = new Document();
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                    new ByteArrayInputStream(content.getBytes()), Charset.forName("UTF-8"));
            document.close();
        } catch (Exception e) {
            System.out.println("------生成pdf失败-------");
            return null;
        }
        return outputStream.toByteArray();
    }

    /**
     * 编辑pdf
     *
     * 传入可编辑表单的pdf(表单中存在文本域,图片域...)
     * 加密
     * @param inputStream  输入文件流
     * @param dataParamMap 要插入得数据
     * @param imgParamMap  要插入得图片
     * @param userPass    打开pdf密码
     * @param ownerPass   编辑pdf密码
     * @param permissions 权限
     * @return PDF字节数组
     */
    public static byte[] exitPdf(InputStream inputStream, Map<String, String> dataParamMap, Map<String, Image> imgParamMap, String userPass, String ownerPass, int permissions) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfUtil.exitPdf(inputStream, dataParamMap, imgParamMap, outputStream, true, userPass, ownerPass, permissions);
        } catch (Exception e) {
            System.out.println("--- exitPdf error ---" + e.fillInStackTrace());
        }
        return outputStream.toByteArray();
    }
    public static byte[] exitPdf(InputStream inputStream, Map<String, String> dataParamMap, Map<String, Image> imgParamMap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfUtil.exitPdf(inputStream, dataParamMap, imgParamMap, outputStream, false, null, null, 0);
        } catch (Exception e) {
            System.out.println("--- exitPdf error ---" + e.fillInStackTrace());
        }
        return outputStream.toByteArray();
    }


    /**
     * 编辑pdf
     *
     * 传入可编辑表单的pdf(表单中存在文本域,图片域...)
     *
     * @param inputStream  输入文件流
     * @param dataParamMap 要插入得数据
     * @param imgParamMap  要插入得图片
     * @param outPdfFilePath  输出文件路径
     * @return 是否创建成功
     */
    public static boolean exitPdf(InputStream inputStream, Map<String, String> dataParamMap, Map<String, Image> imgParamMap, String outPdfFilePath) {
        try {
            FileOutputStream outputStream = new FileOutputStream(outPdfFilePath);
            PdfUtil.exitPdf(inputStream, dataParamMap, imgParamMap, outputStream, false, null, null, 0);
        } catch (Exception e) {
            System.out.println("--- exitPdf error ---" + e.fillInStackTrace());
            return false;
        }
        return true;
    }

    /**
     * @param inputStream  输入文件流
     * @param dataParamMap 要插入得数据
     * @param imgParamMap  要插入得图片
     * @param outputStream 输出文件流
     * @param encryption  是否口令加密
     * @param userPass    打开pdf密码
     * @param ownerPass   编辑pdf密码
     * @param permissions 权限        example: PdfWriter.ALLOW_PRINTING
     * @return
     * @throws Exception
     */
    public static boolean exitPdf(InputStream inputStream, Map<String, String> dataParamMap, Map<String, Image> imgParamMap, OutputStream outputStream, boolean encryption, String userPass, String ownerPass, int permissions) throws Exception{
        BaseFont baseFont = BaseFont.createFont();
        PdfReader pdfReader = new PdfReader(inputStream);
        PdfStamper pdfStamper = new PdfStamper(pdfReader, outputStream);
        AcroFields form = pdfStamper.getAcroFields();
        form.addSubstitutionFont(baseFont);
        // 写入数据
        if (dataParamMap != null) {
            for (String key : dataParamMap.keySet()) {
                String value = dataParamMap.get(key);
                //key对应模板数据域的名称
                form.setField(key, value);
            }
        }
        if (imgParamMap != null) {
            for (Map.Entry<String, Image> entry:imgParamMap.entrySet()) {
                String key = entry.getKey();
                Image value = entry.getValue();
                //添加图片
                int pageNo = form.getFieldPositions(key).get(0).page;
                Rectangle signRect = form.getFieldPositions(key).get(0).position;
                float x = signRect.getLeft();
                float y = signRect.getBottom();
                PdfContentByte under = pdfStamper.getOverContent(pageNo);
                //设置图片大小
                value.scaleAbsolute(signRect.getWidth(), signRect.getHeight());
                //设置图片位置
                value.setAbsolutePosition(x, y);

                under.addImage(value);
            }
        }
        pdfStamper.setFormFlattening(true);
        if (encryption) {
            pdfStamper.setEncryption(true, userPass, ownerPass, permissions);
        }
        pdfStamper.close();
        outputStream.close();
        return true;
    }

    public static boolean exitPdf(PdfParam pdfParam) throws Exception{
        BaseFont baseFont = BaseFont.createFont();
        PdfReader pdfReader = new PdfReader(pdfParam.getInputStream());
        PdfStamper pdfStamper = new PdfStamper(pdfReader, pdfParam.getOutputStream());
        AcroFields form = pdfStamper.getAcroFields();
        form.addSubstitutionFont(baseFont);
        // 写入数据
        if (pdfParam.getDataParamMap() != null) {
            for (String key : pdfParam.getDataParamMap().keySet()) {
                String value = pdfParam.getDataParamMap().get(key);
                //key对应模板数据域的名称
                form.setField(key, value);
            }
        }
        if (pdfParam.getImgParamMap() != null) {
            for (Map.Entry<String, Image> entry:pdfParam.getImgParamMap().entrySet()) {
                String key = entry.getKey();
                Image value = entry.getValue();
                List<AcroFields.FieldPosition> fieldPositions = form.getFieldPositions(key);
                if (fieldPositions != null) {
                    for (AcroFields.FieldPosition fieldPosition:fieldPositions) {
                        //添加图片
                        Rectangle signRect = fieldPosition.position;
                        PdfContentByte under = pdfStamper.getOverContent(fieldPosition.page);
                        //设置图片大小
                        value.scaleAbsolute(signRect.getWidth(), signRect.getHeight());
                        //设置图片位置
                        value.setAbsolutePosition(signRect.getLeft(), signRect.getBottom());
                        under.addImage(value);
                    }
                }
            }
        }
        pdfStamper.setFormFlattening(true);
        if (pdfParam.isEncryption()) {
            pdfStamper.setEncryption(true, pdfParam.getUserPass(), pdfParam.getOwnerPass(), pdfParam.getPermissions());
        }

        pdfStamper.close();
        pdfParam.close();
        return true;
    }

    public static void main(String[] args) throws Exception {
        FileInputStream inputStream = new FileInputStream("D:\\img\\java\\LoanAgreementSanctionTemplate.pdf");
        FileOutputStream outputStream = new FileOutputStream("D:\\img\\java\\test6.pdf");
        PdfParam pdfParam = new PdfParam(inputStream, outputStream);

        Map<String, String> dataMap = new HashMap<>();
        dataMap.put("loan_application_id", "11111111111");

        pdfParam.setDataParamMap(dataMap);
        PdfUtil.exitPdf(pdfParam);

    }
}
