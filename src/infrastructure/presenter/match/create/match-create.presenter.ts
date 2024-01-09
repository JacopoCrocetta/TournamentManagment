import { Logger } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

export class MatchCreatePresenter {
    constructor(private repository: IMatchRepository){}

    async createMatchById(newMatch: Match): Promise<Match>{
        Logger.log("TRYING TO CREATE A NEW MATCH ");
        return this.repository.create(newMatch);
    }
}