package com.boards.api.websockets.events;

import java.util.List;

import com.boards.api.boards.entities.BoardMember;

public record CardWsEvent(
  String event,
  List<BoardMember> boardMembers,
  Long boardId,
  Long cardId,
  Long targetBoardList,
  Long sourceBoardListId,
  Long userId
) {}
