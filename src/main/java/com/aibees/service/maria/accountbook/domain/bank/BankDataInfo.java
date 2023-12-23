package com.aibees.service.maria.accountbook.domain.bank;

import com.aibees.service.maria.accountbook.domain.AbstractDataInfo;
import com.aibees.service.maria.accountbook.domain.DataInfo;
import com.aibees.service.maria.accountbook.entity.vo.BankStatement;
import com.aibees.service.maria.accountbook.util.AccConstant;
import com.aibees.service.maria.accountbook.util.handler.ExcelParseHandler;
import lombok.AllArgsConstructor;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Component
@AllArgsConstructor(staticName = "create")
public class BankDataInfo extends AbstractDataInfo implements DataInfo {

    private List<BankStatement> bankStatements;
    private String type;

    @Override
    public void excelParse(MultipartFile file) throws Exception {
        try {

            Workbook workbook = null;
            String[] fileName = file.getOriginalFilename().split("\\.");
            String fileHashName = this.createFileNameHash();

            if (fileName[fileName.length-1].equals("xlsx")) {
                workbook = new XSSFWorkbook(file.getInputStream());
            } else if (fileName[fileName.length-1].equals("xls")) {
                workbook = new HSSFWorkbook(file.getInputStream());
            }

            if(workbook == null) {
                throw new Exception("workBook is Null");
            }
            bankStatements = (List<BankStatement>) ExcelParseHandler.excelParser(workbook, fileHashName, type).get(AccConstant.CM_RESULT);
        } catch(Exception e) {
                throw new Exception(e.getMessage());
        }
    }
}
