import { Logger } from "@nestjs/common";
import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

export class TournamentPagePresenter{
    constructor(private repository: ITournamentsRepository){}


    async getTournamentById(id: number): Promise<Tournament>{
        Logger.log("RETREIVING TOURNAMENT WITH ID " + id);
        return this.repository.getById(id);
    }

    async getAllTournament():Promise<Tournament[]>{
        Logger.log("RETREIVING ALL TOURNAMENT");
        return this.repository.getAll();
    }
}