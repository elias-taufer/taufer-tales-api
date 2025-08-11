package com.taufer.tales.domain;
import jakarta.persistence.*;
import lombok.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Tale {
  @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
  @Column(nullable=false) private String title;
  @Column(nullable=false) private String author;
  private String isbn;
  @Column(length=4000) private String description;
  private String coverUrl;
  private Integer publishedYear;
  private String tags;
}
