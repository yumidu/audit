package net.hcl.hclhackathon.audit;

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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class Audit extends Auditable{

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id ;
        
        
@NotBlank(groups = {IDataAdd.class},message = "portfolio_id required")
private String portfolio_id ;

@NotBlank(groups = {IDataAdd.class},message = "instrument_id required")
private String instrument_id ;



@NotNull(groups = {IDataAdd.class},message = "quantity required")
@Max(value=1000,groups = {IDataAdd.class,IDataUpdate.class},message = "quantity range error: maximum value allowed is  1000")
private Integer quantity ;

@NotNull(groups = {IDataAdd.class},message = "trade_price required")
private Double trade_price ;

@NotBlank(groups = {IDataAdd.class},message = "trade_type required")
private String trade_type ;





    public String getId() {
        return id;
    }
    public void setId(String val) {
        this.id = val;
    }
        

    public String getPortfolio_id() {
        return portfolio_id;
    }
    public void setPortfolio_id(String val) {
        this.portfolio_id = val;
    }
            

    public String getInstrument_id() {
        return instrument_id;
    }
    public void setInstrument_id(String val) {
        this.instrument_id = val;
    }
            

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer val) {
        this.quantity = val;
    }
            

    public Double getTrade_price() {
        return trade_price;
    }
    public void setTrade_price(Double val) {
        this.trade_price = val;
    }
            

    public String getTrade_type() {
        return trade_type;
    }
    public void setTrade_type(String val) {
        this.trade_type = val;
    }
            


}

