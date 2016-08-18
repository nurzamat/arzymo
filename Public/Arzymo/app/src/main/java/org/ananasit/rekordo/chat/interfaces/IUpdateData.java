package org.ananasit.rekordo.chat.interfaces;

import org.ananasit.rekordo.chat.types.FriendInfo;
import org.ananasit.rekordo.chat.types.MessageInfo;

public interface IUpdateData {
	public void updateData(MessageInfo[] messages, FriendInfo[] friends, FriendInfo[] unApprovedFriends, String userKey);

}
