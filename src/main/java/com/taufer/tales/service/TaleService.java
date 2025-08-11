package com.taufer.tales.service;
import com.taufer.tales.domain.Tale; import com.taufer.tales.dto.*; import com.taufer.tales.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page; import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class TaleService {
  private final TaleRepository tales; private final ReviewRepository reviews;

  public Page<TaleResponse> list(String q, int page, int size){
    var pg = PageRequest.of(page,size);
    var p = (q==null || q.isBlank()) ? tales.findAll(pg) : tales.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(q,q,pg);
    return p.map(t -> new TaleResponse(t.getId(), t.getTitle(), t.getAuthor(), t.getIsbn(), t.getDescription(), t.getCoverUrl(), t.getPublishedYear(), t.getTags(), reviews.avgRating(t.getId())));
  }
  public TaleResponse get(Long id){ var t = tales.findById(id).orElseThrow(); return new TaleResponse(t.getId(), t.getTitle(), t.getAuthor(), t.getIsbn(), t.getDescription(), t.getCoverUrl(), t.getPublishedYear(), t.getTags(), reviews.avgRating(id));}
  public TaleResponse create(TaleCreateDto d){ var t = tales.save(Tale.builder().title(d.title()).author(d.author()).isbn(d.isbn()).description(d.description()).coverUrl(d.coverUrl()).publishedYear(d.publishedYear()).tags(d.tags()).build()); return get(t.getId()); }
  public TaleResponse update(Long id, TaleUpdateDto d){ var t = tales.findById(id).orElseThrow(); if(d.title()!=null) t.setTitle(d.title()); if(d.author()!=null) t.setAuthor(d.author()); if(d.isbn()!=null) t.setIsbn(d.isbn()); if(d.description()!=null) t.setDescription(d.description()); if(d.coverUrl()!=null) t.setCoverUrl(d.coverUrl()); if(d.publishedYear()!=null) t.setPublishedYear(d.publishedYear()); if(d.tags()!=null) t.setTags(d.tags()); return get(tales.save(t).getId()); }
  public void delete(Long id){ tales.deleteById(id); }
}
