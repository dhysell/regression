package repository.cc.framework.gw.cc.policyPlugin;

import repository.cc.framework.gw.cc.policyPlugin.dto.Policy;

public class PolicyPluginResponse {

    Policy policy;

    public PolicyPluginResponse(Policy policy) {
        this.policy = policy;
    }

    public Policy getPolicy() {
        return policy;
    }
}
