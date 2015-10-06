package org.ananasit.arzymo.chat.interfaces;

import org.ananasit.arzymo.chat.types.FriendInfo;
import org.ananasit.arzymo.chat.types.MessageInfo;

public interface IUpdateData {
	public void updateData(MessageInfo[] messages, FriendInfo[] friends, FriendInfo[] unApprovedFriends, String userKey);

}
