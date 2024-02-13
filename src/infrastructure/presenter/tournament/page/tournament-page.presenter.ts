import {  Inject, Logger } from "@nestjs/common";
import { TOURNAMENT_REPOSITORY } from "src/core/repository/tournaments.repository";
import { TournamentsInfrastructureRepository } from "src/infrastructure/repository/tournament/tournaments-infra.repository";

export class TournamentPagePresenter{
    constructor(@Inject(TOURNAMENT_REPOSITORY)private repository: TournamentsInfrastructureRepository){}

    async getTournamentById(id: string){
        Logger.log("[Application] RETREIVING TOURNAMENT WITH ID " + id);
       await this.repository.getById(id) 
    }

    async getAllTournament() {
        Logger.log("[Application] RETREIVING ALL TOURNAMENT");
        return this.repository.getAll();
    }
}