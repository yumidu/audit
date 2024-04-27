package net.hcl.hclhackathon.audit;
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
@RequestMapping(path="audit")
public class AuditController {
    @Autowired

    private final AuditService auditService;
    public AuditController(AuditService auditService){
        this.auditService = auditService;
        
    }

   @GetMapping
   public PagingData<List<Audit>> getAudits(@Validated(IDataSearch.class) AuditQuery auditQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);

            }
            return auditService.getAudits(auditQuery);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }
   
   @GetMapping(path="/{auditId}")
   public Audit getAudit(@PathVariable("auditId") String id){
        try{
            return auditService.getAudit(id);
        }  catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/all")
   public List<Audit> getAuditAll(@Validated(IDataSearch.class) AuditQuery auditQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return auditService.getAuditAll(auditQuery); // LIMIT 200
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
   }

   @GetMapping(path="/suggestions")
   public List<Audit> getAuditSuggestions(@Validated(IDataSearch.class) AuditQuery auditQuery, BindingResult result){
        try{
            if (result.hasErrors()) {
                List<String> errorMessages = result.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                        throw new CustomeException(errorMessages.toString(),null);
            }
            return auditService.getAuditSuggestions(auditQuery);
        }catch(Exception e){  
            throw new CustomeException(e.getMessage(),null);
        }
    }
    
   @PostMapping
   public Audit addNewAudit(@RequestBody @Validated(IDataAdd.class) Audit audit, BindingResult result){
      try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        return auditService.addNewAudit(audit);
      }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
      }
     
   }
   
   

   @DeleteMapping(path="{auditId}")
   public void deleteAudit(@PathVariable("auditId") String id){
         try {
            auditService.deleteAudit(id);
          } catch (Exception e) {
            throw new CustomeException(e.getMessage(),null);
          }
   }

   @PatchMapping(path="{auditId}")
   public void updateAudit(@PathVariable("auditId") String id,@RequestBody Audit audit, BindingResult result){
    try{
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
                    throw new CustomeException(errorMessages.toString(),null);
        }
        auditService.updateAudit(id,audit);
    }catch(Exception e){
        throw new CustomeException(e.getMessage(),null);
    }
   }
}
