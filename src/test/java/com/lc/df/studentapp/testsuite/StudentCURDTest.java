package com.lc.df.studentapp.testsuite;

import com.lc.df.studentapp.model.StudentPojo;
import com.lc.df.studentapp.testbase.TestBase;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.lc.df.studentapp.utils.TestUtils.getRandomValue;
import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertThat;

@RunWith(SerenityRunner.class)

public class StudentCURDTest extends TestBase {

    public static String firstName = "vinay" + getRandomValue();
    public static String lastName = "thakor" + getRandomValue();
    public static String programme = "student Testing";
    public static String email = "xyz" + getRandomValue() + "@gmail.com";
    public static int studentId;
    //


    @Title("This test will create a new student :")
    @Test
    public void test001() {
        List<String> courses = new ArrayList<>();
        courses.add("serenity");
        courses.add("CURD test");

        StudentPojo studentPojo = new StudentPojo();

        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest
                .given()
                .header("Content-Type", "application/json")
                .log()
                .all()
                .body(studentPojo)
                .post()
                .then()
                .log().all()
                .statusCode(201);
    }

    @Title("verify the student is added to the applicatin :")
    @Test
    public void test002() {
        String path1 = "findAll{it.firstName=='";
        String path2 = "'}.get(0)";
       HashMap<String, Object > value = SerenityRest.rest()
                        .given()
                        .when()
                        .get("/list")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path(path1 + firstName + path2);

        assertThat(value, hasValue(firstName));
        studentId = (int) value.get("id");
    }

    @Title("Update the user information and verify the update information :")
    @Test
    public void test003() {

        String path1 = "findAll{it.firstName=='";
        String path2 = "'}.get(0)";

        //update the existing 1st name as its on end to end test
        firstName = firstName + "_putRequest";
        List<String> course = new ArrayList<>();
        course.add("Rename");
        course.add("Computer");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(course);

        SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                .log()
                .all()
                .when()
                .body(studentPojo)
                .put("/" + studentId)
                .then()
                .statusCode(200);

        HashMap<String, Object> value =  SerenityRest.rest()
                        .given()
                        .when()
                        .get("/list")
                        .then()
                        .statusCode(200)
                        .extract()
                        .path(path1 + firstName + path2);
        System.out.println(value);
        assertThat(value, hasValue(firstName));
    }

    @Title("Delete the student and verify if the student is deleted")
    @Test
    public void test004() {
        SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/" + studentId)
                .then()
                .statusCode(204);

        SerenityRest.rest()
                .given()
                .header("Content-Type", "application/json")
                .when()
                .get("/" + studentId)
                .then()
                .statusCode(404);
    }

}

//    @Title("Delete the student and verify if the student is deleted!")
//    @Test
//    public void test04(){
//        SerenityRest.rest()
//                .given()
//                .header("Content-Type", "application/json")
//                //               .contentType(ContentType.JSON)
//                .when()
//                .delete("/"+studentId)
//
//                .then()
//                .statusCode(204);

//        SerenityRest.rest()
//                .given()
//                .header("Content-Type", "application/json")
//                //               .contentType(ContentType.JSON)
//
//                .when()
//                .get("/"+studentId)
//
//                .then()
//                .statusCode(404);
//    }
//}




//}

