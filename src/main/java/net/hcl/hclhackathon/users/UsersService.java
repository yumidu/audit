package net.hcl.hclhackathon.users;
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
public class UsersService {

    private final UsersRepository usersRepository;
    private EntityManager entityManager;

    
    public UsersService(UsersRepository usersRepository,EntityManager entityManager){
        this.usersRepository = usersRepository;
        this.entityManager = entityManager;
        
        
    }
    

    
    public PagingData<List<Users>> getUserss(UsersQuery usersQuery){
        
       Authentication auth = SecurityContextHolder.getContext().getAuthentication();
       List<Query> query=buildUsersQuery(usersQuery);
        
       int size = usersQuery.getLimit()>0 ?usersQuery.getLimit():20;
       int page = usersQuery.getPage()>0 ?usersQuery.getPage():0;
       Object countResult = query.get(1).getSingleResult();
       int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
       List<Users> resultList =  query.get(0).setMaxResults(size).setFirstResult(size*page).getResultList();
       
     return new PagingData<>(Long.valueOf(count),resultList,Long.valueOf(page),size) ;

	}
    public Users getUsers(String id){
        UsersQuery usersQuery=new UsersQuery();
        usersQuery.setId(id);
        usersQuery.setId_mode(QueryEnum.equals);
        
        /*
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         usersQuery.setCreatedBy(username);
         */
        List<Query> query=buildUsersQuery(usersQuery);
        List<Users> resultList =  query.get(0).setMaxResults(1).getResultList();
        if(resultList.size()==0){
           
            throw new CustomeException("NOT_FOUND",null);
        }
        return  resultList.get(0);
	}

    public List<Users> getUsersSuggestions(UsersQuery usersQuery){
       
        String[] cols={"lastname","firstname","email","mobile"};
        usersQuery.setKeywordColumns(cols);
    
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildUsersQuery(usersQuery);
        List<Users> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    public List<Users> getUsersAll(UsersQuery usersQuery){
       
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Query> query=buildUsersQuery(usersQuery);
        List<Users> resultList =  query.get(0).setMaxResults(50).getResultList();
       
     return resultList ;

	}
    


    public Users addNewUsers(Users users) {
       
         Optional<Users> usersByEmail = usersRepository.findUsersByEmail(users.getEmail());
          if(usersByEmail.isPresent()){
              throw new CustomeException("email_NO_AVAILABLE",null);
          }
        
         Optional<Users> usersByMobile = usersRepository.findUsersByMobile(users.getMobile());
          if(usersByMobile.isPresent()){
              throw new CustomeException("mobile_NO_AVAILABLE",null);
          }
        
        return usersRepository.save(users);
    }



    public void deleteUsers(String id) {
        UsersQuery usersQuery=new UsersQuery();
        usersQuery.setId(id);
        usersQuery.setId_mode(QueryEnum.equals);
        /*
        ****** IMPORTANT ******
        //if you want to check data ownership or other factor you check and iplement here
         Authentication auth = SecurityContextHolder.getContext().getAuthentication(); 
         String username = auth.getName();
         usersQuery.setCreateby_mode(QueryEnum.equals);
         usersQuery.setCreatedBy(username);
         */
       
        List<Query> query=buildUsersQuery(usersQuery);
        Object countResult = query.get(1).getSingleResult();
        int count = countResult!= null ? Integer.parseInt(countResult.toString()) : 0;
        if(count==0){
            throw new IllegalStateException("NOT_FOUND");
        }

        usersRepository.deleteById(id);
    }

     @Transactional
     public Users updateUsers(String id, Users users) {
        Users check = usersRepository.findById(id).orElseThrow(()->
           new IllegalStateException("NOT_FOUND"));
          

        if(users.getUsertype()!=null   && !Objects.equals(check.getUsertype(),users.getUsertype())){
            check.setUsertype(users.getUsertype());
        }
                             
    if(users.getLastname()!=null && users.getLastname().length()>0 && !Objects.equals(check.getLastname(),users.getLastname())){
        
        check.setLastname(users.getLastname());
    }
                             
    if(users.getEmailOTP()!=null && users.getEmailOTP().length()>0 && !Objects.equals(check.getEmailOTP(),users.getEmailOTP())){
        
        check.setEmailOTP(users.getEmailOTP());
    }
                             
    if(users.getFirstname()!=null && users.getFirstname().length()>0 && !Objects.equals(check.getFirstname(),users.getFirstname())){
        
        check.setFirstname(users.getFirstname());
    }
                             
    if(users.getPassword()!=null && users.getPassword().length()>0 && !Objects.equals(check.getPassword(),users.getPassword())){
        
        check.setPassword(users.getPassword());
    }
                             
    if(users.getEmail()!=null && users.getEmail().length()>0 && !Objects.equals(check.getEmail(),users.getEmail())){
        
    Optional<Users> checkUsers = usersRepository.findUsersByEmail(users.getEmail());
        if(checkUsers.isPresent()){
            throw new IllegalStateException("Email_NOT_AVAILABLE");
        }
    
        check.setEmail(users.getEmail());
    }
                             
    if(users.getMobile()!=null && users.getMobile().length()>0 && !Objects.equals(check.getMobile(),users.getMobile())){
        
    Optional<Users> checkUsers = usersRepository.findUsersByMobile(users.getMobile());
        if(checkUsers.isPresent()){
            throw new IllegalStateException("Mobile_NOT_AVAILABLE");
        }
    
        check.setMobile(users.getMobile());
    }
  

        if(users.getEmailOTPExpires()!=null   && !Objects.equals(check.getEmailOTPExpires(),users.getEmailOTPExpires())){
            check.setEmailOTPExpires(users.getEmailOTPExpires());
        }
                             
    if(users.getCreateBy()!=null && users.getCreateBy().length()>0 && !Objects.equals(check.getCreateBy(),users.getCreateBy())){
        
        check.setCreateBy(users.getCreateBy());
    }

        if(users.getCreateAt()!=null  && !Objects.equals(check.getCreateAt(),users.getCreateAt())){
            check.setCreateAt(users.getCreateAt());
        }
                             
    if(users.getUpdateBy()!=null && users.getUpdateBy().length()>0 && !Objects.equals(check.getUpdateBy(),users.getUpdateBy())){
        
        check.setUpdateBy(users.getUpdateBy());
    }

        if(users.getUpdateAt()!=null  && !Objects.equals(check.getUpdateAt(),users.getUpdateAt())){
            check.setUpdateAt(users.getUpdateAt());
        }
     return usersRepository.save(check);

    }

   
    private List<Query> buildUsersQuery(UsersQuery usersQuery){
        StringBuilder sb = new StringBuilder();
        StringBuilder sbTotal = new StringBuilder();
        sb.append("SELECT s FROM Users s where 1=1");
        sbTotal.append("SELECT COUNT (s.id) FROM Users s where 1=1 ");
        
        QueryBuilder qb_UsertypeArray = new QueryBuilder("usertype",usersQuery.getUsertype_array());
        if(qb_UsertypeArray.getSql().length()>0){
            sb.append( qb_UsertypeArray.getSql());
            sbTotal.append(qb_UsertypeArray.getSql());
        }
        if(usersQuery.getUsertype()!=null){
            QueryBuilder qb_Usertype = new QueryBuilder("usertype",usersQuery.getUsertype(),usersQuery.getUsertype_mode());
            if(qb_Usertype.getSql().length()>0){
                sb.append( qb_Usertype.getSql());
                sbTotal.append(qb_Usertype.getSql());
            }
        }
        

        QueryBuilder qb_LastnameArray = new QueryBuilder("lastname",usersQuery.getLastname_array());
        if(qb_LastnameArray.getSql().length()>0){
            sb.append( qb_LastnameArray.getSql());
            sbTotal.append(qb_LastnameArray.getSql());
        }
        QueryBuilder qb_Lastname = new QueryBuilder("lastname",usersQuery.getLastname(),usersQuery.getLastname_mode());
        if(qb_Lastname.getSql().length()>0){
            sb.append( qb_Lastname.getSql());
            sbTotal.append(qb_Lastname.getSql());
        }
        

        QueryBuilder qb_EmailOTPArray = new QueryBuilder("emailOTP",usersQuery.getEmailOTP_array());
        if(qb_EmailOTPArray.getSql().length()>0){
            sb.append( qb_EmailOTPArray.getSql());
            sbTotal.append(qb_EmailOTPArray.getSql());
        }
        QueryBuilder qb_EmailOTP = new QueryBuilder("emailOTP",usersQuery.getEmailOTP(),usersQuery.getEmailOTP_mode());
        if(qb_EmailOTP.getSql().length()>0){
            sb.append( qb_EmailOTP.getSql());
            sbTotal.append(qb_EmailOTP.getSql());
        }
        

        QueryBuilder qb_FirstnameArray = new QueryBuilder("firstname",usersQuery.getFirstname_array());
        if(qb_FirstnameArray.getSql().length()>0){
            sb.append( qb_FirstnameArray.getSql());
            sbTotal.append(qb_FirstnameArray.getSql());
        }
        QueryBuilder qb_Firstname = new QueryBuilder("firstname",usersQuery.getFirstname(),usersQuery.getFirstname_mode());
        if(qb_Firstname.getSql().length()>0){
            sb.append( qb_Firstname.getSql());
            sbTotal.append(qb_Firstname.getSql());
        }
        

        QueryBuilder qb_EmailArray = new QueryBuilder("email",usersQuery.getEmail_array());
        if(qb_EmailArray.getSql().length()>0){
            sb.append( qb_EmailArray.getSql());
            sbTotal.append(qb_EmailArray.getSql());
        }
        QueryBuilder qb_Email = new QueryBuilder("email",usersQuery.getEmail(),usersQuery.getEmail_mode());
        if(qb_Email.getSql().length()>0){
            sb.append( qb_Email.getSql());
            sbTotal.append(qb_Email.getSql());
        }
        

        QueryBuilder qb_MobileArray = new QueryBuilder("mobile",usersQuery.getMobile_array());
        if(qb_MobileArray.getSql().length()>0){
            sb.append( qb_MobileArray.getSql());
            sbTotal.append(qb_MobileArray.getSql());
        }
        QueryBuilder qb_Mobile = new QueryBuilder("mobile",usersQuery.getMobile(),usersQuery.getMobile_mode());
        if(qb_Mobile.getSql().length()>0){
            sb.append( qb_Mobile.getSql());
            sbTotal.append(qb_Mobile.getSql());
        }
        

        QueryBuilder qb_IdArray = new QueryBuilder("id",usersQuery.getId_array());
        if(qb_IdArray.getSql().length()>0){
            sb.append( qb_IdArray.getSql());
            sbTotal.append(qb_IdArray.getSql());
        }
        QueryBuilder qb_Id = new QueryBuilder("id",usersQuery.getId(),usersQuery.getId_mode());
        if(qb_Id.getSql().length()>0){
            sb.append( qb_Id.getSql());
            sbTotal.append(qb_Id.getSql());
        }
        

        QueryBuilder qb_EmailOTPExpiresArray = new QueryBuilder("emailOTPExpires",usersQuery.getEmailOTPExpires_array());
        if(qb_EmailOTPExpiresArray.getSql().length()>0){
            sb.append( qb_EmailOTPExpiresArray.getSql());
            sbTotal.append(qb_EmailOTPExpiresArray.getSql());
        }
        if(usersQuery.getEmailOTPExpires()!=null){
            QueryBuilder qb_EmailOTPExpires = new QueryBuilder("emailOTPExpires",usersQuery.getEmailOTPExpires(),usersQuery.getEmailOTPExpires_mode());
            if(qb_EmailOTPExpires.getSql().length()>0){
                sb.append( qb_EmailOTPExpires.getSql());
                sbTotal.append(qb_EmailOTPExpires.getSql());
            }
        }
        

        QueryBuilder qb_CreateByArray = new QueryBuilder("createBy",usersQuery.getCreateBy_array());
        if(qb_CreateByArray.getSql().length()>0){
            sb.append( qb_CreateByArray.getSql());
            sbTotal.append(qb_CreateByArray.getSql());
        }
        QueryBuilder qb_CreateBy = new QueryBuilder("createBy",usersQuery.getCreateBy(),usersQuery.getCreateBy_mode());
        if(qb_CreateBy.getSql().length()>0){
            sb.append( qb_CreateBy.getSql());
            sbTotal.append(qb_CreateBy.getSql());
        }
        

        QueryBuilder qb_UpdateByArray = new QueryBuilder("updateBy",usersQuery.getUpdateBy_array());
        if(qb_UpdateByArray.getSql().length()>0){
            sb.append( qb_UpdateByArray.getSql());
            sbTotal.append(qb_UpdateByArray.getSql());
        }
        QueryBuilder qb_UpdateBy = new QueryBuilder("updateBy",usersQuery.getUpdateBy(),usersQuery.getUpdateBy_mode());
        if(qb_UpdateBy.getSql().length()>0){
            sb.append( qb_UpdateBy.getSql());
            sbTotal.append(qb_UpdateBy.getSql());
        }
        

      QueryBuilder qb_Keyword = new QueryBuilder(usersQuery.getKeywordColumns(),usersQuery.getSearch());
      if(qb_Keyword.getSql().length()>0){
          sb.append( qb_Keyword.getSql());
          sbTotal.append(qb_Keyword.getSql());
      }

          
        // ORDER BY 
        if(usersQuery.getSortBy()!=null && !usersQuery.getSortBy().isEmpty()){
            if(usersQuery.getSortDirection()!=null &&  usersQuery.getSortDirection().toString()!=""){
                String ascDesc = usersQuery.getSortDirection()==1?"ASC":"DESC";
                sb.append( " order by s."+usersQuery.getSortBy()+" "+ascDesc+"");   
            }else{
                sb.append( " order by  s."+usersQuery.getSortBy());
            }
        }
        Query query = entityManager.createQuery(sb.toString(),Users.class);
        Query queryTotal = entityManager.createQuery(sbTotal.toString(),Users.class);
        // Fill Parameters values 

        
        if(usersQuery.getUsertype_array()!=null){
            if(usersQuery.getUsertype_array().length==2){
                query.setParameter("usertype1",usersQuery.getUsertype_array()[0]);
                queryTotal.setParameter("usertype1",usersQuery.getUsertype_array()[0]);

                query.setParameter("usertype2",usersQuery.getUsertype_array()[1]);
                queryTotal.setParameter("usertype2",usersQuery.getUsertype_array()[1]);
            }
        }
        
        if(usersQuery.getUsertype()!=null){
            query.setParameter("usertype",usersQuery.getUsertype());
            queryTotal.setParameter("usertype",usersQuery.getUsertype());
        }
        

        if(usersQuery.getLastname()!=null){
            query.setParameter("lastname",qb_Lastname.getValue());
            queryTotal.setParameter("lastname",qb_Lastname.getValue());
        }
        

        if(usersQuery.getEmailOTP()!=null){
            query.setParameter("emailOTP",qb_EmailOTP.getValue());
            queryTotal.setParameter("emailOTP",qb_EmailOTP.getValue());
        }
        

        if(usersQuery.getFirstname()!=null){
            query.setParameter("firstname",qb_Firstname.getValue());
            queryTotal.setParameter("firstname",qb_Firstname.getValue());
        }
        

        if(usersQuery.getEmail()!=null){
            query.setParameter("email",qb_Email.getValue());
            queryTotal.setParameter("email",qb_Email.getValue());
        }
        

        if(usersQuery.getMobile()!=null){
            query.setParameter("mobile",qb_Mobile.getValue());
            queryTotal.setParameter("mobile",qb_Mobile.getValue());
        }
        

        if(usersQuery.getId()!=null){
            query.setParameter("id",qb_Id.getValue());
            queryTotal.setParameter("id",qb_Id.getValue());
        }
        

        if(usersQuery.getEmailOTPExpires_array()!=null){
            if(usersQuery.getEmailOTPExpires_array().length==2){
                query.setParameter("emailOTPExpires1",usersQuery.getEmailOTPExpires_array()[0]);
                queryTotal.setParameter("emailOTPExpires1",usersQuery.getEmailOTPExpires_array()[0]);

                query.setParameter("emailOTPExpires2",usersQuery.getEmailOTPExpires_array()[1]);
                queryTotal.setParameter("emailOTPExpires2",usersQuery.getEmailOTPExpires_array()[1]);
            }
        }
        
        if(usersQuery.getEmailOTPExpires()!=null){
            query.setParameter("emailOTPExpires",usersQuery.getEmailOTPExpires());
            queryTotal.setParameter("emailOTPExpires",usersQuery.getEmailOTPExpires());
        }
        

        if(usersQuery.getCreateBy()!=null){
            query.setParameter("createBy",qb_CreateBy.getValue());
            queryTotal.setParameter("createBy",qb_CreateBy.getValue());
        }
        

        if(usersQuery.getUpdateBy()!=null){
            query.setParameter("updateBy",qb_UpdateBy.getValue());
            queryTotal.setParameter("updateBy",qb_UpdateBy.getValue());
        }
        

          if(usersQuery.getSearch()!=null){
              query.setParameter("search",qb_Keyword.getValue());
              queryTotal.setParameter("search",qb_Keyword.getValue());
          }
          
        
  
        List<Query> array = new ArrayList<>();
        array.add(query);
        array.add(queryTotal); // ONLY REQUIRED FOR PAGING DATA
        return array;
    }
}
  