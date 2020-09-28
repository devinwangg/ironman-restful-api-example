package com.ironman.demo

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.env.Environment
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement
import java.util.*

/**
 *
 * @author wei-xiang
 * @version 1.0
 * @date 2020/9/25
 */
@RestController
@RequestMapping("/api")
class StudentController(@Autowired val studentDao: StudentDao) {
    /**
     * 取得 Student 所有資料
     */
    @GetMapping("/students")
    fun getStudentData(): MutableList<Student> = studentDao.findAll()

    /**
     * 新增 Student 資料
     */
    @PostMapping("/students")
    fun addStudentData(@RequestBody student: Student): Student  = studentDao.save(student)

    /**
     * 利用姓名查詢學生資料
     */
    @PostMapping("/students/search")
    fun getStudentByName(@RequestParam name: String): ResponseEntity<List<Student>>
            = studentDao
            .findByName(name)
            .let {
                return ResponseEntity(it, HttpStatus.OK)
            }

    /**
     * 修改學生全部資料
     */
    @PutMapping("/students/{id}")
    fun updateStudent(@PathVariable id: Int, @RequestBody student: Student): ResponseEntity<Student?>
            = studentDao
            .findById(id)
            .run {
                this ?: return ResponseEntity<Student?>(null, HttpStatus.NOT_FOUND)
            }.run {
                return ResponseEntity<Student?>(studentDao.save(this), HttpStatus.OK)
            }

    /**
     * 修改學生信箱（欲更新部份資料）
     */
    @PatchMapping("/students/{id}")
    fun updateStudentEmail(@PathVariable id: Int, @RequestBody email: String): ResponseEntity<Student?>
            = studentDao
            .findById(id)
            .run {
                this ?: return ResponseEntity<Student?>(null, HttpStatus.NOT_FOUND)
            }
            .run {
                Student(
                        id = this.id,
                        name = this.name,
                        email = email
                )
            }
            .run {
                return ResponseEntity<Student?>(studentDao.save(this), HttpStatus.OK)
            }

    /**
     * 刪除學生資料
     */
    @DeleteMapping("/students/{id}")
    fun deleteStudent(@PathVariable id: Int): ResponseEntity<Any>
            = studentDao
            .findById(id)
            .run {
                this ?: return ResponseEntity<Any>(null, HttpStatus.NOT_FOUND)
            }
            .run {
                return ResponseEntity<Any>(studentDao.delete(this), HttpStatus.NO_CONTENT)
            }

}