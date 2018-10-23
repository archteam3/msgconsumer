package team.three.msgconsumer.manager.arch;

public interface IArchitecture {
	void build(IMsgBroker broker) throws Exception ;
	void destruct();
}
