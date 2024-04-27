package net.hcl.hclhackathon.users;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import net.hcl.hclhackathon.ucodeutility.QueryEnum;
import net.hcl.hclhackathon.ucodeutility.SortEnum;
public class UsersQuery  extends Users{
	@Getter @Setter private QueryEnum usertype_mode ;
	@Getter @Setter private Number[] usertype_array ;
	@Getter @Setter private QueryEnum lastname_mode ;
	@Getter @Setter private String[] lastname_array ;
	@Getter @Setter private QueryEnum emailOTP_mode ;
	@Getter @Setter private String[] emailOTP_array ;
	@Getter @Setter private QueryEnum firstname_mode ;
	@Getter @Setter private String[] firstname_array ;
	@Getter @Setter private QueryEnum password_mode ;
	@Getter @Setter private String[] password_array ;
	@Getter @Setter private QueryEnum email_mode ;
	@Getter @Setter private String[] email_array ;
	@Getter @Setter private QueryEnum mobile_mode ;
	@Getter @Setter private String[] mobile_array ;
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private String[] id_array ;
	@Getter @Setter private QueryEnum emailOTPExpires_mode ;
	@Getter @Setter private Number[] emailOTPExpires_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private LocalDate[] createAt_array ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private LocalDate[] updateAt_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}