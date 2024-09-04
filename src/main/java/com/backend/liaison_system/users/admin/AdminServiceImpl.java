package com.backend.liaison_system.users.admin;

import com.backend.liaison_system.users.student.Student;
import com.backend.liaison_system.users.student.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public String uploadStudents(MultipartFile file) {
        try {
            log.info("File received: {}", file.getOriginalFilename());
            List<Student> students = new ArrayList<>();
            InputStream inputStream = new BufferedInputStream(file.getInputStream());
            Workbook workbook;
            if (FileMagic.valueOf(inputStream) == FileMagic.OOXML) {
                workbook = new XSSFWorkbook(inputStream); // For `.xlsx` files
            } else if (FileMagic.valueOf(inputStream) == FileMagic.OLE2) {
                workbook = new HSSFWorkbook(inputStream); // For `.xls` files
            } else  {
                return "Error";
            }
            Sheet sheet = workbook.getSheetAt(0);
            for(Row row : sheet) {
                if(row.getRowNum() == 0) continue;
                String password = getCellValueAsString(row.getCell(7));
                Student student = new Student();
                student.setEmail(getCellValueAsString(row.getCell(0)));
                student.setStudentName(getCellValueAsString(row.getCell(1)));
                student.setStudentFaculty(getCellValueAsString(row.getCell(2)));
                student.setStudentDepartment(getCellValueAsString(row.getCell(3)));
                student.setStudentAge(getCellValueAsString(row.getCell(4)));
                student.setStudentGender(getCellValueAsString(row.getCell(5)));
                student.setPassword(passwordEncoder.encode(password));
                students.add(student);
            }
            studentRepository.saveAll(students);
            log.info("Students saved to database");
            return "Students saved to database";
        } catch (Exception e) {
            log.error(e.getMessage());
            return "Error";
        }
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> String.valueOf(cell.getNumericCellValue());
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> "";
        };
    }
}
