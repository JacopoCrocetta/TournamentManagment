import { Logger } from "@nestjs/common";
import { Match } from "src/core/entity/abstract-match";
import { IMatchRepository } from "src/core/repository/match.repository";

export class MatchPagePresenter{
    constructor(private repository: IMatchRepository){}


    async getMatchById(id: number): Promise<Match>{
        Logger.log("RETREIVING MATCH WITH ID " + id);
        return this.repository.getById(id);
    }

    async getAllMatch(tournamentId: number):Promise<Match[]>{
        Logger.log("RETREIVING ALL MTCH FOR MATCH WITH ID " + tournamentId);
        return this.repository.getAllByTournamentId(tournamentId);
    }
}