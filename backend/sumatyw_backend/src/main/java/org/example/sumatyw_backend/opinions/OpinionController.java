package org.example.sumatyw_backend.opinions;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/opinions")
public class OpinionController {

    private OpinionService opinionService;

    @PostMapping()
    public ResponseEntity<OpinionDTO> addOpinion(@RequestBody @Valid OpinionInputDTO opinionInputDTO) {
            Opinion opinion = opinionService.addOpinion(OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

            return new ResponseEntity<>(
                OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
                HttpStatus.OK
            );
    }

    @GetMapping()
    public ResponseEntity<?> getOpinions(@RequestParam(required = false) UUID userId,
                                                        @RequestParam(required = false) UUID restaurantId) {

        if(userId != null && restaurantId != null) {
            return new ResponseEntity<>(opinionService.isRestaurantLikedByUser(userId,restaurantId),HttpStatus.OK);
        }
        List<Opinion> opinions = opinionService.getOpinions();
        return new ResponseEntity<>(opinions.stream().map(OpinionDTOMapper::mapOpinionToOpinionDTO).toList(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OpinionDTO> updateOpinion(@PathVariable("id") UUID id, @RequestBody @Valid OpinionInputDTO opinionInputDTO) {
        Opinion opinion = opinionService.updateOpinionById(id, OpinionDTOMapper.mapOpinionInputDTOToOpinion(opinionInputDTO));

        return new ResponseEntity<>(
            OpinionDTOMapper.mapOpinionToOpinionDTO(opinion),
            HttpStatus.OK
        );
    }
}
