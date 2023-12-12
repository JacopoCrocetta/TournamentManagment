import { Tournament } from "src/core/entity/abstract-tournament";
import { ITournamentsRepository } from "src/core/repository/tournaments.repository";

export class TournamentPagePresenter{
    constructor(private repository: ITournamentsRepository){}


    async getTournamentById(id: number): Promise<Tournament>{
        return this.repository.getById(id);
    }

    async getAllTournament():Promise<Tournament[]>{
        return this.repository.getAll();
    }
}