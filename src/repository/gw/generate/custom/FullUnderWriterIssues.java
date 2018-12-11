package repository.gw.generate.custom;

import repository.gw.enums.UnderwriterIssueType;

import java.util.ArrayList;
import java.util.List;

public class FullUnderWriterIssues {
	
	private List<UnderwriterIssue> informationalList = new ArrayList<UnderwriterIssue>();
	private List<UnderwriterIssue> blockIssuanceList = new ArrayList<UnderwriterIssue>();
	private List<UnderwriterIssue> blockSubmitList = new ArrayList<UnderwriterIssue>();
	private List<UnderwriterIssue> blockQuoteList = new ArrayList<UnderwriterIssue>();
	private List<UnderwriterIssue> blockQuoteReleaseList = new ArrayList<UnderwriterIssue>();
	private List<UnderwriterIssue> rejectList = new ArrayList<UnderwriterIssue>();
	private List<UnderwriterIssue> alreadyApprovedList = new ArrayList<UnderwriterIssue>();
	private boolean allIssuesApproved = false;
	
	public FullUnderWriterIssues() {
		
	}
	
	public boolean noUnderwriterIssues() {
		if(informationalList.size() == 0 && blockIssuanceList.size() == 0 && blockSubmitList.size() == 0 && blockQuoteList.size() == 0 && blockQuoteReleaseList.size() == 0 && rejectList.size() == 0 && alreadyApprovedList.size() == 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isAllIssuesApproved() {
		if(informationalList.size() == 0 && blockIssuanceList.size() == 0 && blockSubmitList.size() == 0 && blockQuoteList.size() == 0 && blockQuoteReleaseList.size() == 0 && rejectList.size() == 0) {
			setAllIssuesApproved(true);
		} else {
			setAllIssuesApproved(false);
		}
		return allIssuesApproved;
	}

	public void setAllIssuesApproved(boolean allIssuesApproved) {
		this.allIssuesApproved = allIssuesApproved;
	}

	public List<UnderwriterIssue> getInformationalList() {
		return informationalList;
	}

	public void setInformationalList(List<UnderwriterIssue> informational) {
		this.informationalList = informational;
	}
	
	public List<UnderwriterIssue> getBlockIssuanceList() {
		return blockIssuanceList;
	}

	public void setBlockIssuanceList(List<UnderwriterIssue> blockIssuance) {
		this.blockIssuanceList = blockIssuance;
	}

	public List<UnderwriterIssue> getBlockSubmitList() {
		return blockSubmitList;
	}

	public void setBlockSubmitList(List<UnderwriterIssue> blockSubmit) {
		this.blockSubmitList = blockSubmit;
	}

	public List<UnderwriterIssue> getBlockQuoteList() {
		return blockQuoteList;
	}

	public void setBlockQuoteList(List<UnderwriterIssue> blockQuote) {
		this.blockQuoteList = blockQuote;
	}

	public List<UnderwriterIssue> getBlockQuoteReleaseList() {
		return blockQuoteReleaseList;
	}

	public void setBlockQuoteReleaseList(List<UnderwriterIssue> blockQuoteRelease) {
		this.blockQuoteReleaseList = blockQuoteRelease;
	}

	public List<UnderwriterIssue> getRejectList() {
		return rejectList;
	}

	public void setRejectList(List<UnderwriterIssue> reject) {
		this.rejectList = reject;
	}
	
	public List<UnderwriterIssue> getAlreadyApprovedList() {
		return alreadyApprovedList;
	}

	public void setAlreadyApprovedList(List<UnderwriterIssue> alreadyApproved) {
		this.alreadyApprovedList = alreadyApproved;
	}
	
	public repository.gw.enums.UnderwriterIssueType isInList_ShortDescription(String uwIssueText) {
		for(UnderwriterIssue issue : informationalList) {
			if(issue.getShortDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.Informational;
			}
		}
		for(UnderwriterIssue issue : blockIssuanceList) {
			if(issue.getShortDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockIssuance;
			}
		}
		for(UnderwriterIssue issue : blockSubmitList) {
			if(issue.getShortDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockSubmit;
			}
		}
		for(UnderwriterIssue issue : blockQuoteList) {
			if(issue.getShortDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockQuote;
			}
		}
		for(UnderwriterIssue issue : blockQuoteReleaseList) {
			if(issue.getShortDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockQuoteRelease;
			}
		}
		for(UnderwriterIssue issue : rejectList) {
			if(issue.getShortDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.Reject;
			}
		}
		for(UnderwriterIssue issue : alreadyApprovedList) {
			if(issue.getShortDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.AlreadyApproved;
			}
		}
		return repository.gw.enums.UnderwriterIssueType.NONE;
	}
	
	public repository.gw.enums.UnderwriterIssueType isInList(String uwIssueText) {
		for(UnderwriterIssue issue : informationalList) {
			if(issue.getLongDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.Informational;
			}
		}
		for(UnderwriterIssue issue : blockIssuanceList) {
			if(issue.getLongDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockIssuance;
			}
		}
		for(UnderwriterIssue issue : blockSubmitList) {
			if(issue.getLongDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockSubmit;
			}
		}
		for(UnderwriterIssue issue : blockQuoteList) {
			if(issue.getLongDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockQuote;
			}
		}
		for(UnderwriterIssue issue : blockQuoteReleaseList) {
			if(issue.getLongDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.BlockQuoteRelease;
			}
		}
		for(UnderwriterIssue issue : rejectList) {
			if(issue.getLongDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.Reject;
			}
		}
		for(UnderwriterIssue issue : alreadyApprovedList) {
			if(issue.getLongDescription().contains(uwIssueText)) {
				return repository.gw.enums.UnderwriterIssueType.AlreadyApproved;
			}
		}
		return UnderwriterIssueType.NONE;
	}
	
	public List<UnderwriterIssue> getAllApprovableUWIssues() {
		List<UnderwriterIssue> returnList = new ArrayList<UnderwriterIssue>();
		returnList.addAll(blockIssuanceList);
		returnList.addAll(blockQuoteList);
		returnList.addAll(blockQuoteReleaseList);
		returnList.addAll(blockSubmitList);
		return returnList;
	}
	
}



















