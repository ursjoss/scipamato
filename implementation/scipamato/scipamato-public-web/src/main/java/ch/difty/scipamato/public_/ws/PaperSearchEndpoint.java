package ch.difty.scipamato.public_.ws;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import ch.difty.scipamato.common.persistence.paging.PaginationContext;
import ch.difty.scipamato.common.persistence.paging.PaginationRequest;
import ch.difty.scipamato.common.persistence.paging.Sort.Direction;
import ch.difty.scipamato.public_.entity.PopulationCode;
import ch.difty.scipamato.public_.entity.PublicPaper;
import ch.difty.scipamato.public_.entity.StudyDesignCode;
import ch.difty.scipamato.public_.entity.filter.PublicPaperFilter;
import ch.difty.scipamato.public_.persistence.api.PublicPaperService;
import lombok.extern.slf4j.Slf4j;

@Endpoint
@Slf4j
public class PaperSearchEndpoint {

    private static final String NAMESPACE_URI = "http://www.difty.ch/scipamato/public";

    private final PublicPaperService service;

    public PaperSearchEndpoint(final PublicPaperService service) {
        this.service = service;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "getPapersRequest")
    @ResponsePayload
    public GetPapersResponse getPapers(@RequestPayload final GetPapersRequest request) {
        log.info("getPapersRequest accepted");
        final GetPapersResponse response = new GetPapersResponse();
        log.info("response instantiated");
        List<PublicPaper> loadPapers = loadPapers(request);
        log.info("papers loaded. Count: {}", loadPapers.size());
        response.papers = translatePublicPapersToPapers(loadPapers);
        log.info("papers set into response. papercount {}", response.getPapers().size());
        return response;
    }

    private List<PublicPaper> loadPapers(GetPapersRequest request) {
        PublicPaperFilter filter = new PublicPaperFilter();
        filter.setNumber(request.getNumber());
        filter.setAuthorMask(request.getAuthorMask());
        filter.setMethodsMask(request.getMethodsMask());
        filter.setPublicationYearFrom(request.getPublicationYearFrom());
        filter.setPublicationYearUntil(request.getPublicationYearUntil());
        filter.setPopulationCodes(toListOfPopulationCodes(request.getPopulationCodes()));
        filter.setStudyDesignCodes(toListOfStudyDesignCodes(request.getStudyDesignCodes()));
        Direction dir = Direction.ASC;
        String sortProp = "number";
        int offset = 0;
        int count = 1000000;
        PaginationContext pc = new PaginationRequest((int) offset, (int) count, dir, sortProp);
        return service.findPageByFilter(filter, pc);
    }

    private List<PopulationCode> toListOfPopulationCodes(List<Short> codes) {
        return codes.stream().map(PopulationCode::of).collect(Collectors.toList());
    }

    private List<StudyDesignCode> toListOfStudyDesignCodes(List<Short> codes) {
        return codes.stream().map(StudyDesignCode::of).collect(Collectors.toList());
    }

    private List<Paper> translatePublicPapersToPapers(final List<PublicPaper> publicPapers) {
        List<Paper> resultList = new ArrayList<>();
        for (final PublicPaper pp : publicPapers) {
            if (pp != null) {
                final Paper paper = new Paper();
                paper.setNumber(pp.getNumber());
                paper.setPmId(pp.getPmId());
                paper.setAuthors(pp.getAuthors());
                paper.setTitle(pp.getTitle());
                paper.setLocation(pp.getLocation());
                paper.setPublicationYear(pp.getPublicationYear());
                paper.setGoals(pp.getGoals());
                paper.setMethods(pp.getMethods());
                paper.setPopulation(pp.getPopulation());
                paper.setResult(pp.getResult());
                paper.setComment(pp.getComment());
                resultList.add(paper);
            }
        }
        return resultList;
    }
}
