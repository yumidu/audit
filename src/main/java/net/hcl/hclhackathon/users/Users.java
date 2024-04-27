package net.hcl.hclhackathon.users;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import org.hibernate.annotations.Type;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import java.time.format.DateTimeParseException;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Size;
import jakarta.persistence.Transient;
import lombok.NoArgsConstructor;
//DB SEPECFIC IMPORT

 import jakarta.persistence.Id;
 import jakarta.persistence.SequenceGenerator;
 import jakarta.persistence.Table;
 import jakarta.persistence.Entity;
 import jakarta.persistence.GeneratedValue;
 import jakarta.persistence.GenerationType;
     
import jakarta.persistence.Column;
import net.hcl.hclhackathon.config.Auditable;
import net.hcl.hclhackathon.interfaces.IDataAdd;
import net.hcl.hclhackathon.interfaces.IDataSearch;
import net.hcl.hclhackathon.interfaces.IDataUpdate;
import net.hcl.hclhackathon.validation.ValidDate;
@Entity
@Table
@NoArgsConstructor
public class Users extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
        
        
@NotNull(groups = {IDataAdd.class},message = "usertype required")
private Integer usertype ;

@NotBlank(groups = {IDataAdd.class},message = "lastname required")
@Size(min=1,max=50,groups = {IDataAdd.class,IDataUpdate.class},message = "lastname length error: should be between 1 and 50 chars")
private String lastname ;

private String emailOTP ;

private String firstname ;

@NotBlank(groups = {IDataAdd.class},message = "password required")
@Size(min=6,max=20,groups = {IDataAdd.class,IDataUpdate.class},message = "password length error: should be between 6 and 20 chars")
private String password ;

@NotBlank(groups = {IDataAdd.class},message = "email required")
@Email(groups = {IDataAdd.class,IDataUpdate.class},message = "email should be valid email address")
private String email ;

@NotBlank(groups = {IDataAdd.class},message = "mobile required")
@Size(min=8,max=20,groups = {IDataAdd.class,IDataUpdate.class},message = "mobile length error: should be between 8 and 20 chars")
private String mobile ;

private Integer emailOTPExpires ;



    public String getId() {
        return id;
    }
    public void setId(String val) {
        this.id = val;
    }
        

    public Integer getUsertype() {
        return usertype;
    }
    public void setUsertype(Integer val) {
        this.usertype = val;
    }
            

    public String getLastname() {
        return lastname;
    }
    public void setLastname(String val) {
        this.lastname = val;
    }
            

    public String getEmailOTP() {
        return emailOTP;
    }
    public void setEmailOTP(String val) {
        this.emailOTP = val;
    }
            

    public String getFirstname() {
        return firstname;
    }
    public void setFirstname(String val) {
        this.firstname = val;
    }
            

    public String getPassword() {
        return password;
    }
    public void setPassword(String val) {
        this.password = val;
    }
            

    public String getEmail() {
        return email;
    }
    public void setEmail(String val) {
        this.email = val;
    }
            

    public String getMobile() {
        return mobile;
    }
    public void setMobile(String val) {
        this.mobile = val;
    }
            

    public Integer getEmailOTPExpires() {
        return emailOTPExpires;
    }
    public void setEmailOTPExpires(Integer val) {
        this.emailOTPExpires = val;
    }
            


}

