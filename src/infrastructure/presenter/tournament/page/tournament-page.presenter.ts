import { Logger } from "@nestjs/common";
import { TournamentsInfrastructureRepository } from "src/infrastructure/repository/tournament/tournaments-infra.repository";

export class TournamentPagePresenter{
    constructor(private repository: TournamentsInfrastructureRepository){}

    async getTournamentById(id: string){
        Logger.log("RETREIVING TOURNAMENT WITH ID " + id);
       await this.repository.getById({id}) 
    }

    async getAllTournament() {
        return this.repository.getAll();
    }
}