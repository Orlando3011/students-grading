package com.pl.studentsGrading.service;

import com.pl.studentsGrading.exception.IdNotNullException;
import com.pl.studentsGrading.exception.StudentNotFoundException;
import com.pl.studentsGrading.model.Student;
import com.pl.studentsGrading.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;

    private Student testStudent;

    @BeforeEach
    void setUp() {
        testStudent = new Student();
        testStudent.setId(1L);
        testStudent.setFirstName("Jan");
        testStudent.setLastName("Kowalski");
        testStudent.setEmail("jan.kowalski@example.com");
    }

    @Test
    void getAllStudents_ShouldReturnListOfStudents() {
        // given
        List<Student> expectedStudents = Collections.singletonList(testStudent);
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // when
        List<Student> actualStudents = studentService.getAllStudents();

        // then
        assertThat(actualStudents).isEqualTo(expectedStudents);
        verify(studentRepository).findAll();
    }

    @Test
    void getStudentById_WhenStudentExists_ShouldReturnStudent() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // when
        Optional<Student> result = studentService.getStudentById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(testStudent);
        verify(studentRepository).findById(1L);
    }

    @Test
    void getStudentById_WhenStudentDoesNotExist_ShouldReturnEmpty() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        Optional<Student> result = studentService.getStudentById(1L);

        // then
        assertThat(result).isEmpty();
        verify(studentRepository).findById(1L);
    }

    @Test
    void createStudent_WithValidStudent_ShouldReturnSavedStudent() {
        // given
        Student newStudent = new Student();
        newStudent.setFirstName("Anna");
        newStudent.setLastName("Nowak");
        newStudent.setEmail("anna.nowak@example.com");
        
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // when
        Student result = studentService.createStudent(newStudent);

        // then
        assertThat(result).isEqualTo(newStudent);
        verify(studentRepository).save(newStudent);
    }

    @Test
    void createStudent_WithNonNullId_ShouldThrowIllegalArgumentException() {
        // given
        Student invalidStudent = new Student();
        invalidStudent.setId(1L);

        // when/then
        assertThatThrownBy(() -> studentService.createStudent(invalidStudent))
                .isInstanceOf(IdNotNullException.class)
                .hasMessage("Id must be null but is: " + invalidStudent.getId());
        
        verify(studentRepository, never()).save(any());
    }

    @Test
    void updateStudent_WhenStudentExists_ShouldReturnUpdatedStudent() {
        // given
        Student updatedStudent = new Student();
        updatedStudent.setFirstName("Updated");
        updatedStudent.setLastName("Student");
        updatedStudent.setEmail("updated.student@example.com");

        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        // when
        Student result = studentService.updateStudent(1L, updatedStudent);

        // then
        assertThat(result).isEqualTo(updatedStudent);
        verify(studentRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void updateStudent_WhenStudentDoesNotExist_ShouldThrowStudentNotFoundException() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> studentService.updateStudent(1L, new Student()))
                .isInstanceOf(StudentNotFoundException.class);
        
        verify(studentRepository).findById(1L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void deleteStudent_WhenStudentExists_ShouldDeleteStudent() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // when
        studentService.deleteStudent(1L);

        // then
        verify(studentRepository).findById(1L);
        verify(studentRepository).delete(testStudent);
    }

    @Test
    void deleteStudent_WhenStudentDoesNotExist_ShouldThrowStudentNotFoundException() {
        // given
        when(studentRepository.findById(1L)).thenReturn(Optional.empty());

        // when/then
        assertThatThrownBy(() -> studentService.deleteStudent(1L))
                .isInstanceOf(StudentNotFoundException.class);
        
        verify(studentRepository).findById(1L);
        verify(studentRepository, never()).delete(any());
    }
}