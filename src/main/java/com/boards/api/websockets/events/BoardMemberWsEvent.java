package com.boards.api.websockets.events;


public record BoardMemberWsEvent(
  String event,
  Long targetUserId,
  Long boardId  
) {}

