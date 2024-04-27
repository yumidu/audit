package net.hcl.hclhackathon.users;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import net.hcl.hclhackathon.ucodeutility.PagingData;
import net.hcl.hclhackathon.exception.CustomeException;
import net.hcl.hclhackathon.interfaces.IDataAdd;
import net.hcl.hclhackathon.interfaces.IDataUpdate;
import net.hcl.hclhackathon.interfaces.IDataSearch;
import net.hcl.hclhackathon.message.ResponseMessage;
import net.hcl.hclhackathon.ucodeutility.QueryEnum;

import net.hcl.hclhackathon.service.FilesStorageService;

@RestController
@RequestMapping(path="users")
public class UsersController {
    @Autowired

    private final UsersService usersService;
    public UsersController(UsersService usersService){
        this.usersService = usersService;
        
    }

   @GetMapping
   public PagingData<List<Users>> getUserss(@Validated(IDataSearch.class) UsersQuery usersQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return usersService.getUserss(usersQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{usersId}")
   public Users getUsers(@PathVariable("usersId") String id){
        try{
            return usersService.getUsers(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<Users> getUsersAll(@Validated(IDataSearch.class) UsersQuery usersQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return usersService.getUsersAll(usersQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<Users> getUsersSuggestions(@Validated(IDataSearch.class) UsersQuery usersQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return usersService.getUsersSuggestions(usersQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public Users addNewUsers(@RequestBody @Validated(IDataAdd.class) Users users, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return usersService.addNewUsers(users);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
        @PostMapping(path="/reg")
        public Users addNewUsers(@RequestBody @Validated(IDataAdd.class) Users users){
           try{
             return usersService.addNewUsers(users);
           }catch(Exception e){
             throw new CustomeException(e.getMessage(),null);
           }
          
        }
    
   

   @DeleteMapping(path="{usersId}")
   public void deleteUsers(@PathVariable("usersId") String id){
         try {
            usersService.deleteUsers(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{usersId}")
   public void updateUsers(@PathVariable("usersId") String id,@RequestBody Users users, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        usersService.updateUsers(id,users);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
