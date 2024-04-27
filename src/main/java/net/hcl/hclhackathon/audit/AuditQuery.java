package net.hcl.hclhackathon.audit;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import net.hcl.hclhackathon.ucodeutility.QueryEnum;
import net.hcl.hclhackathon.ucodeutility.SortEnum;
public class AuditQuery  extends Audit{
	@Getter @Setter private QueryEnum id_mode ;
	@Getter @Setter private String[] id_array ;
	@Getter @Setter private QueryEnum createBy_mode ;
	@Getter @Setter private String[] createBy_array ;
	@Getter @Setter private QueryEnum createAt_mode ;
	@Getter @Setter private QueryEnum updateBy_mode ;
	@Getter @Setter private String[] updateBy_array ;
	@Getter @Setter private QueryEnum updateAt_mode ;
	@Getter @Setter private QueryEnum portfolio_id_mode ;
	@Getter @Setter private String[] portfolio_id_array ;
	@Getter @Setter private QueryEnum instrument_id_mode ;
	@Getter @Setter private String[] instrument_id_array ;
	@Getter @Setter private QueryEnum quantity_mode ;
	@Getter @Setter private Number[] quantity_array ;
	@Getter @Setter private QueryEnum trade_price_mode ;
	@Getter @Setter private Number[] trade_price_array ;
	@Getter @Setter private QueryEnum trade_type_mode ;
	@Getter @Setter private String[] trade_type_array ;

    @Getter @Setter private String sortBy ;
    @Getter @Setter private Integer sortDirection ;
    @Getter @Setter private int page ;
    @Getter @Setter private int limit ;
    @Getter @Setter private String[] keywordColumns ;
    @Getter @Setter private String search ;
}