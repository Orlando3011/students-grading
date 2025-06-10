package com.pl.studentsGrading.service;

import com.pl.studentsGrading.exception.StudentNotFoundException;
import com.pl.studentsGrading.model.Student;
import com.pl.studentsGrading.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    @Autowired
    private final StudentRepository studentRepository;

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

    public Student createStudent(Student student) {
        if (student.getId() != null) {
            throw new IllegalArgumentException("ID must be null");
        }
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, Student updatedStudent) {
        return studentRepository.findById(id)
                .map(existingStudent -> {
                    existingStudent.setFirstName(updatedStudent.getFirstName());
                    existingStudent.setLastName(updatedStudent.getLastName());
                    existingStudent.setEmail(updatedStudent.getEmail());
                    existingStudent.setPersonalId(updatedStudent.getPersonalId());
                    existingStudent.setPhoneNumber(updatedStudent.getPhoneNumber());
                    existingStudent.setAddress(updatedStudent.getAddress());
                    existingStudent.setDateOfBirth(updatedStudent.getDateOfBirth());
                    return studentRepository.save(existingStudent);
                })
                .orElseThrow(() -> new StudentNotFoundException(id));
    }


    public void deleteStudent(Long id) {
        studentRepository.findById(id)
                .ifPresentOrElse(
                        studentRepository::delete,
                        () -> {
                            throw new StudentNotFoundException(id);
                        }
                );
    }
}
