package com.tickethandler.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tickethandler.dto.TicketResolveResponse;
import com.tickethandler.dto.TicketResolverDto;
import com.tickethandler.dto.TicketResponse;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.Company;
import com.tickethandler.model.Product;
import com.tickethandler.model.Requester;
import com.tickethandler.model.SupportEngineer;
import com.tickethandler.model.Ticket;
import com.tickethandler.model.TicketStatus;
import com.tickethandler.model.TicketType;
import com.tickethandler.model.TicketsLog;
import com.tickethandler.repo.CompanyRepository;
import com.tickethandler.repo.ProductRepository;
import com.tickethandler.repo.RequesterRepository;
import com.tickethandler.repo.SupportEngineerRepository;
import com.tickethandler.repo.TicketRepository;
import com.tickethandler.repo.TicketStatusRepository;
import com.tickethandler.repo.TicketTypeRepository;
import com.tickethandler.repo.TicketsLogRepository;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketStatusRepository ticketStatusRepository;
    
    @Autowired
    private SupportEngineerRepository supportEngineerRepository;

    @Autowired
    private TicketsLogRepository ticketsLogRepository;
    
    @Autowired
    private RequesterRepository requesterRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TicketTypeRepository ticketTypeRepository;

    @Autowired
    private ProductRepository productRepository;
    
//    @Autowired
//    private CompanyProductsRepository companyProductsRepository;
//    
    
    
    
    public List<Ticket> getTicketsByCompanyName(String companyName) {
        return ticketRepository.findByCompany_CompanyName(companyName);
    }

    public List<Ticket> getTicketsByProductName(String productName) {
        return ticketRepository.findByProduct_ProductName(productName);
    }
    
    


    //@Transactional
    public TicketResponse createTicket(int requesterId, int companyId, int ticketTypeId, int productId, String ticketSummary, String ticketDetails, int ticketPriority) {
        // Fetch the required entities from the repository
    	//validation to make sure the company and product are the same of requerster
        Requester requester = requesterRepository.findById(requesterId).orElseThrow(() -> new ResourceNotFoundException("Requester not found"));
        Company company = companyRepository.findById(companyId).orElseThrow(() -> new RuntimeException("Company not found"));
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId).orElseThrow(() -> new RuntimeException("Ticket Type not found"));
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));
        TicketStatus ticketStatus = ticketStatusRepository.findById(1).orElseThrow(() -> new RuntimeException("Initial Ticket Status not found"));
        //Company company = requester.getCompany();
        //int productId = companyProductsRepository.findProductIdByCompanyId(company.getCompanyId());
        //Product product = productRepository.findById(productId);
        
        
        Ticket ticket = new Ticket();
        ticket.setTicketDate(new Date());
        ticket.setRequester(requester);
        ticket.setCompany(company);
        ticket.setTicketType(ticketType);
        ticket.setProduct(product);
        ticket.setTicketSummary(ticketSummary);
        ticket.setTicketDetails(ticketDetails);
        ticket.setTicketPriority(ticketPriority);
        ticket.setTicketStatus(ticketStatus);
        
         ticketRepository.save(ticket);
         
         //return the info that the user needs
         TicketResponse ticketDto = new TicketResponse();
         ticketDto.setTicketId(ticket.getTicketNo());
         ticketDto.setTicketTypeId(ticketTypeId);
         ticketDto.setTicketPriority(ticketPriority);
         ticketDto.setTicketDetails(ticketDetails);
         ticketDto.setTicketSummary(ticketSummary);
         
         return ticketDto;
    }

    @Transactional
    public TicketResolverDto assignTicket(Long ticketNo, int engineerId) {
        Ticket ticket = ticketRepository.findById(ticketNo)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        SupportEngineer engineer = supportEngineerRepository.findById(engineerId)
            .orElseThrow(() -> new ResourceNotFoundException("Support Engineer not found"));

        ticket.setAssigendTo(engineer);
        
        TicketStatus inProgressStatus = ticketStatusRepository.findByStatusName("In Progress");
            //.orElseThrow(() -> new ResourceNotFoundException("In Progress status not found"));
        ticket.setTicketStatus(inProgressStatus);

         ticketRepository.save(ticket);
         
         //the data that will appear to the user
         TicketResolverDto resolver = new TicketResolverDto();
         resolver.setTicketNo(ticketNo);
         resolver.setRequesterId(ticket.getRequester().getRequesterId());
         resolver.setRequesterName(ticket.getRequester().getRequesterName());
         resolver.setCompanyId(ticket.getCompany().getCompanyId());
         resolver.setCompanyName(ticket.getCompany().getCompanyName());
         resolver.setTicketTypeId(ticket.getTicketType().getTypeId());
         resolver.setTicketTypeName(ticket.getTicketType().getTypeName());
         resolver.setProductId(ticket.getProduct().getProductId());
         resolver.setTicketSummary(ticket.getTicketSummary());
         resolver.setTicketDetails(ticket.getTicketDetails());
         resolver.setTicketPriority(ticket.getTicketPriority());
         resolver.setEngineerId(ticket.getAssigendTo().getEngineerId());
         resolver.setEngineerName(ticket.getAssigendTo().getEngineerName());
        
         
         return resolver;
   }    
    
    
    @Transactional
    public TicketResolveResponse resolveTicketAndAddLog(Long ticketNo, int engineerId, String logDetails) {
        Ticket ticket = ticketRepository.findById(ticketNo)
            .orElseThrow(() -> new RuntimeException("Ticket not found"));
        SupportEngineer engineer = supportEngineerRepository.findById(engineerId)
            .orElseThrow(() -> new RuntimeException("Support Engineer not found"));

        
        ticket.setResolvedBy(engineer);
        ticket.setResolutionDate(LocalDateTime.now());
        TicketStatus resolvedStatus = ticketStatusRepository.findByStatusName("closed");
            //.orElseThrow(() -> new RuntimeException("Resolved status not found"));
        ticket.setTicketStatus(resolvedStatus);

        // Add log
        TicketsLog log = new TicketsLog();
        log.setTicket(ticket);
        log.setLogDate(LocalDateTime.now());
        log.setLogedBy(engineer);
        log.setLogDetails(logDetails);

        ticketsLogRepository.save(log);
        ticketRepository.save(ticket);
       
        TicketResolveResponse ticketResponse = new TicketResolveResponse();
        ticketResponse.setTicketId(ticketNo);
        ticketResponse.setTicketTypeId(ticket.getTicketType().getTypeId());
        ticketResponse.setTicketSummary(ticket.getTicketSummary());
        ticketResponse.setLogDetails(logDetails);
        return ticketResponse;
    }
    
    
    
    
   
    
}
