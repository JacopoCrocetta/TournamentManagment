import { Inject, Injectable, Logger } from "@nestjs/common";
import { Participants } from "src/core/entity/abstract-participants";
import { IParticipantsRepository } from "src/core/repository/participants.repository";

@Injectable()
export class PartecipantCreate {
  constructor(@Inject() private participantsRepository: IParticipantsRepository) {}

  create = async (input: Participants): Promise<Participants> => {
    Logger.log("CREATING A NEW PARTICIPANT...");
    const createdParticipant = await this.participantsRepository.create(input);

    Logger.log("CHECKING IF THE MATCH WAS CREATED");
    if (!!createdParticipant) {
      Logger.log("MATCH WAS NOT CREATED CORRECTLY");
      return null;
    }

    Logger.log("MATCH CREATED CORRECTLY");
    return createdParticipant;
  };
}
