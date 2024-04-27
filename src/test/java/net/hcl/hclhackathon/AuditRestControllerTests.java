
package net.hcl.hclhackathon;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cglib.core.Local;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import net.hcl.hclhackathon.auth.AuthenticationResponse;
import net.hcl.hclhackathon.audit.Audit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import net.hcl.hclhackathon.ucodeutility.PagingData;
import java.time.LocalDateTime;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuditRestControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    private String token; //Auth token
    private Audit oneAudit= new Audit();   
    
    private void loginAndGetToken() throws Exception {
        String response = mockMvc.perform(MockMvcRequestBuilders.post("/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\": \"test@ucode.ai\", \"password\": \"123456\" }"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        this.token = extractTokenFromResponse(response);
    }
    private String extractTokenFromResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            AuthenticationResponse json = objectMapper.readValue(response, AuthenticationResponse.class);
            return "";//json.getAccessToken();
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    private String getAccessToken() {
        return "";
        // TODO
    }

    @Test
    public void testAddAudit() throws Exception {
        if(this.token==null){
        loginAndGetToken();
        }
            oneAudit.setPortfolio_id("cillum");
    oneAudit.setInstrument_id("enim");
    oneAudit.setQuantity(4);
    oneAudit.setTrade_price(1.35);
    oneAudit.setTrade_type("sitclf");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "portfolio_id", oneAudit.getPortfolio_id()!=null?oneAudit.getPortfolio_id():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instrument_id", oneAudit.getInstrument_id()!=null?oneAudit.getInstrument_id():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "quantity", oneAudit.getQuantity()>0?oneAudit.getQuantity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "trade_price", oneAudit.getTrade_price()>0?oneAudit.getTrade_price():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "trade_type", oneAudit.getTrade_type()!=null?oneAudit.getTrade_type():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/audit")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio_id").value(Matchers.containsString(oneAudit.getPortfolio_id())))
        .andDo(MockMvcResultHandlers.print());
    }
    @Test
    public void testGetAudits() throws Exception {

        if(oneAudit.getPortfolio_id()==null){
            GetOneAudit();
        }
        mockMvc.perform(MockMvcRequestBuilders.get("/audit")
                    .param("quantity", Integer.toString(oneAudit.getQuantity()))
        .param("quantity_array",Integer.toString(oneAudit.getQuantity()-1))
        .param("quantity_array",Integer.toString(oneAudit.getQuantity()+1))
        .param("trade_price", Double.toString(oneAudit.getTrade_price()))
        .param("trade_price_array",Double.toString(oneAudit.getTrade_price()-1))
        .param("trade_price_array",Double.toString(oneAudit.getTrade_price()+1))
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.docs[0].name").value(Matchers.containsStringIgnoringCase(sortenString(oneAudit.getPortfolio_id()))))
            .andDo(MockMvcResultHandlers.print());
    }
    
    
    
    @Test
    public void testAddAuditByMaxQuantity_1000() throws Exception {
        if(this.oneAudit.getPortfolio_id()==null){
            GetOneAudit();
        }
        Audit reqAudit = new Audit();
            reqAudit.setPortfolio_id("adipisicing");
    reqAudit.setInstrument_id("dolore");
    reqAudit.setQuantity(1);
    reqAudit.setTrade_price(1.35);
    reqAudit.setTrade_type("nostrud");
        reqAudit.setQuantity(1001); // set  unfit value 
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "portfolio_id", reqAudit.getPortfolio_id()!=null?reqAudit.getPortfolio_id():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instrument_id", reqAudit.getInstrument_id()!=null?reqAudit.getInstrument_id():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "quantity", reqAudit.getQuantity()>0?reqAudit.getQuantity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "trade_price", reqAudit.getTrade_price()>0?reqAudit.getTrade_price():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "trade_type", reqAudit.getTrade_type()!=null?reqAudit.getTrade_type():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");

        mockMvc.perform(MockMvcRequestBuilders.post("/audit")
        .accept(MediaType.APPLICATION_JSON)
        .content(requestBody.toString()).contentType(MediaType.APPLICATION_JSON)
        .header("Authorization", "Bearer "+token))
        .andExpect(MockMvcResultMatchers.status().is5xxServerError())
        .andExpect(MockMvcResultMatchers.jsonPath("$.errors").value(Matchers.containsStringIgnoringCase("quantity range error:")))
        .andDo(MockMvcResultHandlers.print());
    }
        
    
    
    
    @Test  // 
    public void testUpdateAuditByID() throws Exception {
        if(oneAudit.getPortfolio_id()==null){
            GetOneAudit();
        }
        Audit updateAudit = new Audit();
            updateAudit.setPortfolio_id("reprehenderit");
    updateAudit.setInstrument_id("euclf");
    updateAudit.setQuantity(1);
    updateAudit.setTrade_price(1.35);
    updateAudit.setTrade_type("elit");
        StringBuilder requestBody = new StringBuilder();
        List<String> jsonData = new ArrayList<String>();
              jsonData.add(String.format("\"%s\":\"%s\"", "portfolio_id", updateAudit.getPortfolio_id()!=null?updateAudit.getPortfolio_id():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "instrument_id", updateAudit.getInstrument_id()!=null?updateAudit.getInstrument_id():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "quantity", updateAudit.getQuantity()>0?updateAudit.getQuantity():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "trade_price", updateAudit.getTrade_price()>0?updateAudit.getTrade_price():""));
      jsonData.add(String.format("\"%s\":\"%s\"", "trade_type", updateAudit.getTrade_type()!=null?updateAudit.getTrade_type():""));
        requestBody.append("{");
        requestBody.append(String.join(", ", jsonData));
        requestBody.append("}");
        String path = "/audit/"+oneAudit.getId();
        mockMvc.perform(MockMvcRequestBuilders.put(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio_id").value(Matchers.containsString(updateAudit.getPortfolio_id())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    public void testGetAuditByID() throws Exception {
        if(oneAudit.getPortfolio_id()==null){
        GetOneAudit();
        }
       
        String path = "/audit/"+oneAudit.getId();
        mockMvc.perform(MockMvcRequestBuilders.get(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.portfolio_id").value(Matchers.containsString(oneAudit.getPortfolio_id())))
                .andDo(MockMvcResultHandlers.print());
    
    }
    @Test
    public void testDeleteAuditByID() throws Exception {
        if(oneAudit.getPortfolio_id()==null){
            GetOneAudit();
        }
        String path = "/audit/"+oneAudit.getId();
        mockMvc.perform(MockMvcRequestBuilders.delete(path)
                .accept(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer "+token))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    
    }
    String sortenString(String val){
        return val.length()>3? val.substring(1, val.length()-1):val;
    }
    private void GetOneAudit() throws Exception {
      if(this.token==null){
        loginAndGetToken();
      }
        String response = mockMvc.perform(MockMvcRequestBuilders.get("/audit")
            .accept(MediaType.APPLICATION_JSON)
            .header("Authorization", "Bearer "+token))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andDo(MockMvcResultHandlers.print())
            .andReturn()
            .getResponse()
            .getContentAsString();
            @SuppressWarnings("unchecked")
        PagingData<List<LinkedHashMap<String, Object>>> oneAudit_ = objectMapper.readValue(response,PagingData.class);
        if(oneAudit_.getDocs().size()>0){
        int i =oneAudit_.getDocs().size();
        List<LinkedHashMap<String, Object>> one= oneAudit_.getDocs();
        Audit obj =convertToAudit( one.get(i-1)); // last one
        this.oneAudit = obj;
        }
     }
    private static Audit convertToAudit(LinkedHashMap<String, Object> entry) {
        // Extract the necessary data from the LinkedHashMap and create a Audit object
        
        // Retrieve other properties as needed
        Audit objCls = new Audit();
        
          String portfolio_id = (String) entry.get("portfolio_id");
          objCls.setPortfolio_id(portfolio_id);
              

          String instrument_id = (String) entry.get("instrument_id");
          objCls.setInstrument_id(instrument_id);
              

          String quantity = (String) entry.get("quantity");
          Integer quantity_ = Integer.parseInt(quantity);
          objCls.setQuantity(quantity_);
                      

          String trade_price = (String) entry.get("trade_price");
          double trade_price_ = Double.parseDouble(trade_price);
          objCls.setTrade_price(trade_price_);
                  

          String trade_type = (String) entry.get("trade_type");
          objCls.setTrade_type(trade_type);
              
        
    String id = (String)entry.get("id");
    objCls.setId(id);
    
        return objCls;
    }
    String  arrayToString(String[] array){
        String format = String.join(", ", Collections.nCopies(array.length, "\"%s\""));
        String formattedString = String.format(format, (Object[]) array);
        String formatted=String.format("[%s]",  formattedString);
       return  formatted ;
    }

}
    