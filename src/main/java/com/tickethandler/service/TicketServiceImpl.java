package com.tickethandler.service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tickethandler.dto.CompanyDto;
import com.tickethandler.dto.TicketResolveResponse;
import com.tickethandler.dto.TicketResolverDto;
import com.tickethandler.dto.TicketResponse;
import com.tickethandler.dto.TicketUpdateRequest;
import com.tickethandler.exception.ResourceNotFoundException;
import com.tickethandler.model.Company;
import com.tickethandler.model.Product;
import com.tickethandler.model.Requester;
import com.tickethandler.model.SupportEngineer;
import com.tickethandler.model.Ticket;
import com.tickethandler.model.TicketStatus;
import com.tickethandler.model.TicketType;
import com.tickethandler.model.TicketsLog;
import com.tickethandler.model.UserEntity;
import com.tickethandler.repo.CompanyRepository;
import com.tickethandler.repo.ProductRepository;
import com.tickethandler.repo.RequesterRepository;
import com.tickethandler.repo.SupportEngineerRepository;
import com.tickethandler.repo.TicketRepository;
import com.tickethandler.repo.TicketStatusRepository;
import com.tickethandler.repo.TicketTypeRepository;
import com.tickethandler.repo.TicketsLogRepository;
import com.tickethandler.repo.UserRepository;

@Service
public class TicketServiceImpl implements TicketService {



	private TicketRepository ticketRepository;
    private TicketStatusRepository ticketStatusRepository;
    private UserRepository userRepository;
    private TicketsLogRepository ticketsLogRepository;
    private CompanyService companyService;
    private ProductService productService;
    private TicketTypeRepository ticketTypeRepository;
    private ProductRepository productRepository;
    
 
	
    public TicketServiceImpl(TicketRepository ticketRepository, TicketStatusRepository ticketStatusRepository,
			UserRepository userRepository, TicketsLogRepository ticketsLogRepository,
			CompanyService companyService, TicketTypeRepository ticketTypeRepository,
			ProductService productService,
			ProductRepository productRepository) {
		this.ticketRepository = ticketRepository;
		this.ticketStatusRepository = ticketStatusRepository;
		this.userRepository = userRepository;
		this.ticketsLogRepository = ticketsLogRepository;
		this.companyService = companyService;
		this.productService=productService;
		this.ticketTypeRepository = ticketTypeRepository;
		this.productRepository = productRepository;
	}

    
    
    public List<TicketResponse> getTicketsByCompany() {
    	
    	CompanyDto company=companyService.getCompanyByid();
    	List<Ticket> tickets = ticketRepository.
    			findByCompany_CompanyName(company.getCompany_name());
    	List<TicketResponse> ticketDto = tickets.stream()
    			.map(this::mapTicketToResponse)
    			.collect(Collectors.toList());
    	return ticketDto;
    }

    public List<TicketResponse> getTicketsByProductName(String productName) {
    	List<Ticket> tickets = ticketRepository.findByProduct_ProductName(productName);
        List<TicketResponse> ticketDto = tickets.stream()
    			.map(this::mapTicketToResponse)
    			.collect(Collectors.toList());
    	return ticketDto;
    }
    
    


    @Transactional
    public TicketResponse createTicket( int ticketTypeId,
    		int productId, String ticketSummary, String ticketDetails, int ticketPriority) {
        // Fetch the required entities from the repository
    	
    	//validation to make sure the company and product are the same of requerster
       
        
        TicketType ticketType = ticketTypeRepository.findById(ticketTypeId).
        		orElseThrow(() -> new ResourceNotFoundException("Ticket Type not found"));
        Product product = productRepository.findById(productId).
        		orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        TicketStatus ticketStatus = ticketStatusRepository.findById(1).
        		orElseThrow(() -> new ResourceNotFoundException("Initial Ticket Status not found"));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        Requester requester = (Requester)userRepository.findByemail(username).
        		orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Company company = requester.getCompany();
        
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
         TicketResponse ticketDto = mapTicketToResponse(ticket);
       
         
         return ticketDto;
    }

    @Transactional
    public TicketResolverDto assignTicket(Long ticketNo) {
        Ticket ticket = ticketRepository.findById(ticketNo)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SupportEngineer engineer = (SupportEngineer) userRepository.findByemail(username).
        		orElseThrow(() -> new UsernameNotFoundException("User not found"));

        ticket.setAssigendTo(engineer);
        
        TicketStatus inProgressStatus = ticketStatusRepository.findByStatusName("IN PROGRES");
            //.orElseThrow(() -> new ResourceNotFoundException("In Progress status not found"));
        ticket.setTicketStatus(inProgressStatus);

         ticketRepository.save(ticket);
         
         //the data that will appear to the user
         TicketResolverDto resolver = new TicketResolverDto();
         resolver.setTicketNo(ticketNo);
         resolver.setRequesterId(ticket.getRequester().getId());
         resolver.setRequesterName(ticket.getRequester().getFullName());
         resolver.setCompanyId(ticket.getCompany().getCompanyId());
         resolver.setCompanyName(ticket.getCompany().getCompanyName());
         resolver.setTicketTypeId(ticket.getTicketType().getTypeId());
         resolver.setTicketTypeName(ticket.getTicketType().getTypeName());
         resolver.setProductId(ticket.getProduct().getProductId());
         resolver.setTicketSummary(ticket.getTicketSummary());
         resolver.setTicketDetails(ticket.getTicketDetails());
         resolver.setTicketPriority(ticket.getTicketPriority());
         resolver.setEngineerId(ticket.getAssigendTo().getId());
         resolver.setEngineerName(ticket.getAssigendTo().getFullName());
        
         
         return resolver;
   }    
    
    
    @Transactional
    public TicketResolveResponse resolveTicketAndAddLog(Long ticketNo, String logDetails) {
        Ticket ticket = ticketRepository.findById(ticketNo)
            .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        SupportEngineer engineer = (SupportEngineer) userRepository.findByemail(username).
        		orElseThrow(() -> new UsernameNotFoundException("User not found"));


        
        ticket.setResolvedBy(engineer);
        ticket.setResolutionDate(LocalDateTime.now());
        TicketStatus resolvedStatus = ticketStatusRepository.findByStatusName("RESOLVED");
            //.orElseThrow(() -> new ResourceNotFoundException("Resolved status not found"));
        ticket.setTicketStatus(resolvedStatus);

        
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
    


	public TicketResponse updateTicket(Long ticketNo, TicketUpdateRequest updateRequest) {
		
		Ticket ticket = ticketRepository.findById(ticketNo).orElseThrow(
				()-> new ResourceNotFoundException("ticket with id: "+ticketNo+" doesn't exist"));

		if(!updateRequest.getTicketDetails().isEmpty()||
				!updateRequest.getTicketSummary().isEmpty()) {
			if(!updateRequest.getTicketDetails().isEmpty()) {
				ticket.setTicketDetails(updateRequest.getTicketDetails());
			}
			if(!updateRequest.getTicketSummary().isEmpty()) {
				ticket.setTicketSummary(updateRequest.getTicketSummary());
			}
			
			ticketRepository.save(ticket);
		}else {
			throw new RuntimeException("Empty parametars! can't update ticket");
		}
		
		
		return mapTicketToResponse(ticket);
	}
    
    public void deleteTicket(long ticketNo) {
    	Ticket ticket = ticketRepository.findById(ticketNo)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket not found"));
    	ticketRepository.delete(ticket);
    }
    
    private TicketResponse mapTicketToResponse(Ticket ticket) {
    	
    	TicketResponse ticketDto = new TicketResponse();
        ticketDto.setTicketId(ticket.getTicketNo());
        ticketDto.setTicketTypeId(ticket.getTicketType().getTypeId());
        ticketDto.setTicketPriority(ticket.getTicketPriority());
        ticketDto.setTicketDetails(ticket.getTicketDetails());
        ticketDto.setTicketSummary(ticket.getTicketSummary());
        
        return ticketDto;
    }






    
    
    
   
    
}
