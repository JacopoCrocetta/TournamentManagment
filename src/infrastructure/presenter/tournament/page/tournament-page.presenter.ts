import { Injectable } from "@nestjs/common";
import { TournamentPage } from "src/core/use-case/tournament/tournament-page";
import { Tournament } from "src/core/entity/abstract-tournament";

@Injectable()
export class TournamentPagePresenter{
    constructor(private tournamentPage:TournamentPage){}


    async getTournamentById(id: number): Promise<Tournament>{
        return this.tournamentPage.getById(id);
    }

    async getAllTournament():Promise<Tournament[]>{
        return this.tournamentPage.getAll();
    }
}