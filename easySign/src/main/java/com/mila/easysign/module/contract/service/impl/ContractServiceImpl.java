package com.mila.easysign.module.contract.service.impl;

import com.mila.easysign.module.contract.entity.SampleContract;
import com.mila.easysign.module.contract.service.ContractService;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.lang.Contract;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class ContractServiceImpl implements ContractService {

    @Override
    public byte[] getContract(SampleContract userData) {
        // 创建模板文件
        try {
            ClassPathResource templates = new ClassPathResource("templates/template1.docx");
            InputStream inputStream = templates.getInputStream();

            try (XWPFDocument xwpfDocument = new XWPFDocument(inputStream)) {
                Map<String, String> dataMap = new HashMap<>();
                dataMap.put("name", userData.getName());
                dataMap.put("age", userData.getAge());
                dataMap.put("address", userData.getAddress());
                dataMap.put("phone", userData.getPhone());
                dataMap.put("email", userData.getEmail());


                replaceInParagraphs(xwpfDocument.getParagraphs(), dataMap);

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                xwpfDocument.write(outputStream);

                return outputStream.toByteArray();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void replaceInParagraphs(List<XWPFParagraph> paragraphs, Map<String, String> dataMap) {
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();

            // 检查段落是否包含占位符
            if (text != null && text.contains("{")) {
                for (Map.Entry<String, String> entry : dataMap.entrySet()) {
                    String placeholder = "{" + entry.getKey() + "}";
                    if (text.contains(placeholder)) {
                        // 替换占位符
                        text = text.replace(placeholder, entry.getValue());

                        // 清除原有内容
                        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
                            paragraph.removeRun(i);
                        }

                        // 添加新内容
                        XWPFRun newRun = paragraph.createRun();
                        newRun.setText(text);
                        break;
                    }
                }
            }
        }
    }
}
