package com.boards.api.cards.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.boards.api.cards.dtos.CardResponseDto;
import com.boards.api.cards.dtos.CreateCardDto;
import com.boards.api.cards.dtos.UpdateCardDto;
import com.boards.api.cards.entities.Card;

@Mapper(componentModel = "spring")
public interface CardMapper {
  // @Mapping(target = "id", ignore = true)
  // @Mapping(target = "position", ignore = true)
  Card toEntity(CreateCardDto createCardDto);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateEntity(UpdateCardDto updateCardDto, @MappingTarget Card card);

  CardResponseDto toResponseDto(Card card);
}
