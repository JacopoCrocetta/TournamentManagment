import { Logger } from "@nestjs/common";
import { Teams } from "src/core/entity/abstract-teams";
import { ITeamsRepository } from "src/core/repository/teams.repository";

export class TeamsPagePresenter{
    constructor(private repository: ITeamsRepository){}


    async getTeamsById(id: number): Promise<Teams>{
        Logger.log("RETREIVING TEAMS WITH ID " + id);
        return this.repository.getById(id);
    }

    async getAllTeams():Promise<Teams[]>{
        Logger.log("RETREIVING ALL TEAMS FOR REFEREES WITH ID ");
        return this.repository.getAll();
    }
}