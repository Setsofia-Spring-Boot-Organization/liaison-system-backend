package com.backend.liaison_system.users.student.supervision.suoervision_report;

import com.backend.liaison_system.users.admin.dto.StudentDto;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;

@Component
public class SupervisionReportGenerator {

    private Set<StudentDto> students;
    private XSSFWorkbook xssfWorkbook;
    private XSSFSheet xssfSheet;

    public SupervisionReportGenerator(Set<StudentDto> students) {
        this.students = students;
        xssfWorkbook = new XSSFWorkbook();
    }

    private void writeHeader() {
        xssfSheet = xssfWorkbook.createSheet("Students");
        Row row = xssfSheet.createRow(0);
        CellStyle cellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setBold(true);
        xssfFont.setFontHeight(16);
        cellStyle.setFont(xssfFont);
        createCell(row, 0, "Student ID", cellStyle);
        createCell(row, 1, "Student Name", cellStyle);
        createCell(row, 2, "Student Email", cellStyle);
        createCell(row, 3, "Student Phone", cellStyle);
        createCell(row, 4, "Faculty", cellStyle);
        createCell(row, 5, "Department", cellStyle);
        createCell(row, 6, "Course", cellStyle);
        createCell(row, 7, "Place Of Attachment", cellStyle);
    }

    private void createCell(Row row, int columnCount, Object cellValue, CellStyle cellStyle) {
        xssfSheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (cellValue instanceof Integer) {
            cell.setCellValue((Integer) cellValue);
        } else if (cellValue instanceof Long) {
            cell.setCellValue((Long) cellValue);
        } else if (cellValue instanceof String) {
            cell.setCellValue((String) cellValue);
        } else {
            cell.setCellValue((Boolean) cellValue);
        }
        cell.setCellStyle(cellStyle);
    }

    private void writeFile() {
        int rowCount = 1;
        CellStyle cellStyle = xssfWorkbook.createCellStyle();
        XSSFFont xssfFont = xssfWorkbook.createFont();
        xssfFont.setFontHeight(14);
        cellStyle.setFont(xssfFont);

        students.forEach(
            student -> {
                Row row = xssfSheet.createRow(rowCount+1);
                int columnCount = 0;

                createCell(row, columnCount+1, student.id(), cellStyle);
                createCell(row, columnCount+1, student.name(), cellStyle);
                createCell(row, columnCount+1, student.email(), cellStyle);
                createCell(row, columnCount+1, student.phone(), cellStyle);
                createCell(row, columnCount+1, student.faculty(), cellStyle);
                createCell(row, columnCount+1, student.department(), cellStyle);
                createCell(row, columnCount+1, student.course(), cellStyle);
                createCell(row, columnCount+1, student.placeOfInternship(), cellStyle);
            }
        );
    }

    public void generateSupervisionReport(HttpServletResponse response) throws IOException {
        writeHeader();
        writeFile();

        ServletOutputStream outputStream = response.getOutputStream();
        xssfWorkbook.write(outputStream);
        xssfWorkbook.close();
        outputStream.close();
    }
}
