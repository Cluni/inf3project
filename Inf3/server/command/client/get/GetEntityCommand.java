package command.client.get;

import server.TcpClient;
import server.Server;
import tokenizer.ITokenizable;
import util.ServerConst;
import command.ClientCommand;
import environment.entity.Dragon;
import environment.entity.Entity;
import environment.entity.Player;
import environment.wrapper.ServerDragon;
import environment.wrapper.ServerPlayer;

public class GetEntityCommand extends ClientCommand {

	public GetEntityCommand(Server _server) {
		super(_server, ServerConst.GET_ENTITY);
	}

	@Override
	protected int routine(TcpClient _src, String _cmd, StringBuilder _mes) {
		int result = 1;
		_src.beginMessage();
		try {
			int id = Integer.parseInt(_cmd);
			Entity ent = Entity.getEntity(id);
			if(ent != null) {
				ITokenizable tok;
				if(ent instanceof Player) {
					tok = new ServerPlayer((Player)ent, server, false);
				} else {
					tok = new ServerDragon((Dragon)ent, server, false);
				}
				_src.sendTokenizable(tok);
				_mes.append("sent entity "+id+" to "+_src);
			}
			else {
				_src.send(ServerConst.ANS+ServerConst.ANS_INVALID);
			}
		} catch(NumberFormatException nfe) {
			_src.send(ServerConst.ANS+ServerConst.ANS_INVALID);
			result = -1;
		} finally {
			_src.endMessage();
		}
		return result;
	}
}
