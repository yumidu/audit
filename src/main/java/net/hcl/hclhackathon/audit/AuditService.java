package net.hcl.hclhackathon.audit;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import net.hcl.hclhackathon.exception.CustomeException;
import net.hcl.hclhackathon.ucodeutility.QueryBuilder;
import net.hcl.hclhackathon.ucodeutility.FieldType;
import net.hcl.hclhackathon.ucodeutility.QueryEnum;
import net.hcl.hclhackathon.ucodeutility.PagingData;

@Service
public class AuditService {

    private final AuditRepository auditRepository;
    private EntityManager entityManager;

    
    public AuditService(AuditRepository auditRepository,EntityManager entityManager){
        this.auditRepository = auditRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Audit>> getAudits(AuditQuery auditQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildAuditQuery(auditQuery);
        
       int size = auditQuery.getLimit()>0 ?auditQuery.getLimit():20;
       int page = auditQuery.getPage()>0 ?auditQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Audit> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Audit getAudit(String id){
        AuditQuery auditQuery=new AuditQuery();
        auditQuery.setId(id);
        auditQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         auditQuery.setCreatedBy(username);
         */
        List<Query> query=buildAuditQuery(auditQuery);
        List<Audit> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Audit> getAuditSuggestions(AuditQuery auditQuery){
       
        String[] cols={"portfolio_id","instrument_id","trade_type"};
        auditQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAuditQuery(auditQuery);
        List<Audit> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Audit> getAuditAll(AuditQuery auditQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildAuditQuery(auditQuery);
        List<Audit> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Audit addNewAudit(Audit audit) {
       
        return auditRepository.save(audit);
    }



    public void deleteAudit(String id) {
        AuditQuery auditQuery=new AuditQuery();
        auditQuery.setId(id);
        auditQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         auditQuery.setCreateby_mode(QueryEnum.equals);
         auditQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildAuditQuery(auditQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        auditRepository.deleteById(id);
    }

     @Transactional
     public Audit updateAudit(String id, Audit audit) {
        Audit check = auditRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
                                     
    if(audit.getCreateBy()!=null && audit.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),audit.getCreateBy())){
        
        check.setCreateBy(audit.getCreateBy());
    }

        if(audit.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),audit.getCreateAt())){
            check.setCreateAt(audit.getCreateAt());
        }
                             
    if(audit.getUpdateBy()!=null && audit.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),audit.getUpdateBy())){
        
        check.setUpdateBy(audit.getUpdateBy());
    }

        if(audit.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),audit.getUpdateAt())){
            check.setUpdateAt(audit.getUpdateAt());
        }
                             
    if(audit.getPortfolio_id()!=null && audit.getPortfolio_id().length()>0 && !Objects.equals(check.getPortfolio_id(),audit.getPortfolio_id())){
        
        check.setPortfolio_id(audit.getPortfolio_id());
    }
                             
    if(audit.getInstrument_id()!=null && audit.getInstrument_id().length()>0 && !Objects.equals(check.getInstrument_id(),audit.getInstrument_id())){
        
        check.setInstrument_id(audit.getInstrument_id());
    }
  

        if(audit.getQuantity()!=null   && !Objects.equals(check.getQuantity(),audit.getQuantity())){
            check.setQuantity(audit.getQuantity());
        }

        if(audit.getTrade_price()!=null  && !Objects.equals(check.getTrade_price(),audit.getTrade_price())){
            check.setTrade_price(audit.getTrade_price());
        }
                             
    if(audit.getTrade_type()!=null && audit.getTrade_type().length()>0 && !Objects.equals(check.getTrade_type(),audit.getTrade_type())){
        
        check.setTrade_type(audit.getTrade_type());
    }
     return auditRepository.save(check);

    }

   
    private List<Query> buildAuditQuery(AuditQuery auditQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Audit s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Audit s where 1=1 ");
        
        QueryBuilder qb_IdArray = new QueryBuilder("id",auditQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        QueryBuilder qb_Id = new QueryBuilder("id",auditQuery.getId(),auditQuery.getId_mode());
        if(qb_Id.getSql().length()>0){
            sb.append( qb_Id.getSql());
            sbTotal.append(qb_Id.getSql());
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",auditQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",auditQuery.getCreateBy(),auditQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",auditQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",auditQuery.getUpdateBy(),auditQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

        QueryBuilder qb_Portfolio_idArray = new QueryBuilder("portfolio_id",auditQuery.getPortfolio_id_array());
        if(qb_Portfolio_idArray.getSql().length()>0){
            sb.append( qb_Portfolio_idArray.getSql());
            sbTotal.append(qb_Portfolio_idArray.getSql());
        }
        QueryBuilder qb_Portfolio_id = new QueryBuilder("portfolio_id",auditQuery.getPortfolio_id(),auditQuery.getPortfolio_id_mode());
        if(qb_Portfolio_id.getSql().length()>0){
            sb.append( qb_Portfolio_id.getSql());
            sbTotal.append(qb_Portfolio_id.getSql());
        }
        

        QueryBuilder qb_Instrument_idArray = new QueryBuilder("instrument_id",auditQuery.getInstrument_id_array());
        if(qb_Instrument_idArray.getSql().length()>0){
            sb.append( qb_Instrument_idArray.getSql());
            sbTotal.append(qb_Instrument_idArray.getSql());
        }
        QueryBuilder qb_Instrument_id = new QueryBuilder("instrument_id",auditQuery.getInstrument_id(),auditQuery.getInstrument_id_mode());
        if(qb_Instrument_id.getSql().length()>0){
            sb.append( qb_Instrument_id.getSql());
            sbTotal.append(qb_Instrument_id.getSql());
        }
        

        QueryBuilder qb_QuantityArray = new QueryBuilder("quantity",auditQuery.getQuantity_array());
        if(qb_QuantityArray.getSql().length()>0){
            sb.append( qb_QuantityArray.getSql());
            sbTotal.append(qb_QuantityArray.getSql());
        }
        if(auditQuery.getQuantity()!=null){
            QueryBuilder qb_Quantity = new QueryBuilder("quantity",auditQuery.getQuantity(),auditQuery.getQuantity_mode());
            if(qb_Quantity.getSql().length()>0){
                sb.append( qb_Quantity.getSql());
                sbTotal.append(qb_Quantity.getSql());
            }
        }
        

        QueryBuilder qb_Trade_priceArray = new QueryBuilder("trade_price",auditQuery.getTrade_price_array());
        if(qb_Trade_priceArray.getSql().length()>0){
            sb.append( qb_Trade_priceArray.getSql());
            sbTotal.append(qb_Trade_priceArray.getSql());
        }
        if(auditQuery.getTrade_price()!=null){
            QueryBuilder qb_Trade_price = new QueryBuilder("trade_price",auditQuery.getTrade_price(),auditQuery.getTrade_price_mode());
            if(qb_Trade_price.getSql().length()>0){
                sb.append( qb_Trade_price.getSql());
                sbTotal.append(qb_Trade_price.getSql());
            }
        }
        

        QueryBuilder qb_Trade_typeArray = new QueryBuilder("trade_type",auditQuery.getTrade_type_array());
        if(qb_Trade_typeArray.getSql().length()>0){
            sb.append( qb_Trade_typeArray.getSql());
            sbTotal.append(qb_Trade_typeArray.getSql());
        }
        QueryBuilder qb_Trade_type = new QueryBuilder("trade_type",auditQuery.getTrade_type(),auditQuery.getTrade_type_mode());
        if(qb_Trade_type.getSql().length()>0){
            sb.append( qb_Trade_type.getSql());
            sbTotal.append(qb_Trade_type.getSql());
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(auditQuery.getKeywordColumns(),auditQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(auditQuery.getSortBy()!=null && !auditQuery.getSortBy().isEmpty()){
            if(auditQuery.getSortDirection()!=null &&  auditQuery.getSortDirection().toString()!=""){
                String ascDesc = auditQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+auditQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+auditQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Audit.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Audit.class);
        // Fill Parameters values 

        
        if(auditQuery.getId()!=null){
            query.setParameter("id",qb_Id.getValue());
            queryTotal.setParameter("id",qb_Id.getValue());
        }
        

        if(auditQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(auditQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

        if(auditQuery.getPortfolio_id()!=null){
            query.setParameter("portfolio_id",qb_Portfolio_id.getValue());
            queryTotal.setParameter("portfolio_id",qb_Portfolio_id.getValue());
        }
        

        if(auditQuery.getInstrument_id()!=null){
            query.setParameter("instrument_id",qb_Instrument_id.getValue());
            queryTotal.setParameter("instrument_id",qb_Instrument_id.getValue());
        }
        

        if(auditQuery.getQuantity_array()!=null){
            if(auditQuery.getQuantity_array().length==2){
                query.setParameter("quantity1",auditQuery.getQuantity_array()[0]);
                queryTotal.setParameter("quantity1",auditQuery.getQuantity_array()[0]);

                query.setParameter("quantity2",auditQuery.getQuantity_array()[1]);
                queryTotal.setParameter("quantity2",auditQuery.getQuantity_array()[1]);
            }
        }
        
        if(auditQuery.getQuantity()!=null){
            query.setParameter("quantity",auditQuery.getQuantity());
            queryTotal.setParameter("quantity",auditQuery.getQuantity());
        }
        

        if(auditQuery.getTrade_price_array()!=null){
            if(auditQuery.getTrade_price_array().length==2){
                query.setParameter("trade_price1",auditQuery.getTrade_price_array()[0]);
                queryTotal.setParameter("trade_price1",auditQuery.getTrade_price_array()[0]);

                query.setParameter("trade_price2",auditQuery.getTrade_price_array()[1]);
                queryTotal.setParameter("trade_price2",auditQuery.getTrade_price_array()[1]);
            }
        }
        
        if(auditQuery.getTrade_price()!=null){
            query.setParameter("trade_price",auditQuery.getTrade_price());
            queryTotal.setParameter("trade_price",auditQuery.getTrade_price());
        }
        

        if(auditQuery.getTrade_type()!=null){
            query.setParameter("trade_type",qb_Trade_type.getValue());
            queryTotal.setParameter("trade_type",qb_Trade_type.getValue());
        }
        

          if(auditQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  