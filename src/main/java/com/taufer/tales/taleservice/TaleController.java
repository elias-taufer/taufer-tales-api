package com.taufer.tales.taleservice;
import com.taufer.tales.dto.*; import com.taufer.tales.service.TaleService;
import jakarta.validation.Valid; import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/tales") @RequiredArgsConstructor
public class TaleController {
  private final TaleService svc;
  @GetMapping public Page<TaleResponse> list(@RequestParam(required=false) String q, @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size){ return svc.list(q,page,size); }
  @GetMapping("/{id}") public TaleResponse get(@PathVariable Long id){ return svc.get(id); }
  @PostMapping public TaleResponse create(@RequestBody @Valid TaleCreateDto d){ return svc.create(d); }
  @PatchMapping("/{id}") public TaleResponse update(@PathVariable Long id, @RequestBody TaleUpdateDto d){ return svc.update(id,d); }
  @DeleteMapping("/{id}") public void delete(@PathVariable Long id){ svc.delete(id); }
}
