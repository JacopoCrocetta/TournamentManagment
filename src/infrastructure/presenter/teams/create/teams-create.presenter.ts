import { Logger } from "@nestjs/common";
import { ExceptionsHandler } from "@nestjs/core/exceptions/exceptions-handler";
import { Teams } from "src/core/entity/abstract-teams";
import { ITeamsRepository } from "src/core/repository/teams.repository";

export class TeamsCreatePresenter {
    constructor(private repository: ITeamsRepository){}

    async createMatchById(newTeams: Teams): Promise<Teams>{
        Logger.log("TRYING TO CREATE A NEW TEAMS...");

        const createdTeams = await this.repository.create(newTeams);

        if(!!createdTeams){
            Logger.log("TEAMS WITH ID "+  createdTeams.id+ " CREATED SUCCESSFULLY");
            return createdTeams;
        }
        
        Logger.log("TEAMS NOT CREATED SUCCESSFULLY");
        throw new ExceptionsHandler();
    }
}