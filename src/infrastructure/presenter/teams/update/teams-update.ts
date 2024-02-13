import { Logger } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

export class TeamsUpdatePresenter {
    constructor(private repository: IMatchRepository){}

    async updateMatchById(matchToUpdate: Match):Promise<Match>{
        Logger.log("TRYING TO UPDATE THE MATCH WITH ID " + matchToUpdate.id);

        const refereesUpdated = await this.repository.update(matchToUpdate);

        Logger.log("MATCH WITH ID " + refereesUpdated.id+ " IS UPDATED");
        return refereesUpdated;
    }
}