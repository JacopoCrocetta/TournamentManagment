import { Logger } from "@nestjs/common";
import { Standings } from "src/core/entity/abstract-standings";
import { IStandingRepository } from "src/core/repository/standings.repository";

export class StandingsPagePresenter{
    constructor(private repository: IStandingRepository){}


    async getStandingsById(id: number): Promise<Standings>{
        Logger.log("RETREIVING REFEREES WITH ID " + id);
        return this.repository.getById(id);
    }

    async getAllStandingsByTournamentId(tournamentId: number):Promise<Standings[]>{
        Logger.log("RETREIVING ALL STANDING FOR TOURNAMENT WITH ID " + tournamentId);
        return this.repository.getAllByIdTournament(tournamentId);
    }
}