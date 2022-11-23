export interface player {
    playerId:number
    username:string,
    nickname?:string,
    playerImage?:string,
    nationality?:string,
    number?:number,
    position?:string
    gamertag?:string,
    discordId?:string
}

export interface team {
    teamId:number
    name: string,
    language: string,
    kit?: string,
    gkKit?: string,
    mainColor: string,
    secondaryColor: string,
    teamLogo: string,
    background: string,
    lineupStyle: string,
    fontColor: string,
    font: string,
    manager: string
}

export interface user {
    username:string,
    password:string,
    email:string,
    userId:number,
    discordId?:string
}
