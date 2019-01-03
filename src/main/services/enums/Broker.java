package services.enums;

public enum Broker {
    DEV("mqdev.idfbins.com", "mbdev.idfbins.com", "1420", "MQ75DEV", "GWASDEV.MQ75DEV"),
    IT("mqit.idfbins.com", "mbit.idfbins.com", "1420", "MQ75IT", "GWASIT.MQ75IT"),
    QA("mqqa.idfbins.com", "mbqa.idfbins.com", "1420", "MQ75QA", "GWASQA.MQ75QA"),
    UAT("mquat.idfbins.com", "mbuat.idfbins.com", "1420", "MQ75UAT", "GWASUAT.MQ75UAT"),
    PRD("mqprd.idfbins.com", "mbprd.idfbins.com", "1420", "MQ75PRD", "GWASPRD.MQ75PRD"),
    RAFAEL("10.230.1.64", "10.230.1.64", "2414", "MB8QMGR", "SYSTEM.DEF.SVRCONN"),
    CHRIS("fbmis790vm-sand.idfbins.com", "fbmis790vm-sand.idfbins.com", "2414", "MB8QMGR", "SYSTEM.DEF.SVRCONN");

    String mqHost;
    String mbHost;
    String port;
    String queueManager;
    String channel;

    private Broker(String mqHost, String mbHost, String port, String queueManager, String channel) {
        this.mqHost = mqHost;
        this.mbHost = mbHost;
        this.port = port;
        this.queueManager = queueManager;
        this.channel = channel;
    }

    public String getMQHost() {
        return mqHost;
    }

    public String getMBHost() {
        return mbHost;
    }

    public String getPort() {
        return port;
    }

    public String getQueueManager() {
        return queueManager;
    }

    public String getChannel() {
        return channel;
	}

    public Broker getBroker(String environment) {
        Broker broker = Broker.DEV;
        switch (environment) {
            case "QA":
            case "QA2":
                broker = Broker.QA;
                break;
            case "DEV":
            case "DEV2":
                broker = Broker.DEV;
                break;
            case "IT":
            case "IT2":
                broker = Broker.IT;
                break;
            case "UAT":
            case "UAT2":
                broker = Broker.UAT;
                break;
            default:
                broker = Broker.DEV;
        }
        return broker;
	}
}
