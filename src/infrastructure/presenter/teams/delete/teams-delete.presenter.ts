import { Logger } from "@nestjs/common";
import { ITeamsRepository } from "src/core/repository/teams.repository";

export class TeamsDeletePresenter {
    constructor(private repository: ITeamsRepository){}

    async deleteTeamsById(id: number): Promise<boolean>{
        Logger.log("TRYING TO DELETE THE MATCH WITH ID " + id);
        return await this.repository.delete(id);
    }
}